package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CleaningServices
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.FiberManualRecord
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.ArduinoProject
import com.example.ui.components.CyberPanel
import com.example.ui.components.CyberStatusBadge
import com.example.ui.components.NeonButton
import com.example.ui.theme.CyberDarkCard
import com.example.ui.theme.CyberDarkSurface
import com.example.ui.theme.CyberSuccess
import com.example.ui.theme.CyberTextMuted
import com.example.ui.theme.CyberTextPrimary
import com.example.ui.theme.CyberTextSecondary
import com.example.ui.theme.LocalKarenTheme
import com.example.ui.theme.OrbitronFontFamily
import com.example.viewmodel.KarenOSViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class SerialLogEntry(
    val timestamp: String,
    val text: String,
    val type: SerialType
)

enum class SerialType {
    RX, TX, INFO, WARN
}

data class ArduinoCodeSnippet(
    val title: String,
    val category: String,
    val description: String,
    val code: String
)

/**
 * Compose component for the 'Arduino Studio' section featuring:
 * 1. Simulated Serial Monitor Window with baud rate selector, TX/RX console, auto-scroll, clear & quick commands.
 * 2. Arduino Code Snippet Display Area with syntax layout, copy, line numbers, and preset selector.
 */
@Composable
fun ArduinoSerialMonitorSnippetView(
    activeProject: ArduinoProject,
    viewModel: KarenOSViewModel,
    modifier: Modifier = Modifier
) {
    val theme = LocalKarenTheme.current
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val timeFormat = remember { SimpleDateFormat("HH:mm:ss.SSS", Locale.US) }

    // Serial Monitor State
    val baudRates = listOf(9600, 19200, 38400, 57600, 115200)
    var selectedBaud by remember { mutableIntStateOf(115200) }
    var isStreaming by remember { mutableStateOf(true) }
    var serialInput by remember { mutableStateOf("") }
    val serialLogs = remember {
        mutableStateListOf(
            SerialLogEntry(timeFormat.format(Date()), "--- SERIAL MONITOR INITIALIZED @ 115200 BAUD ---", SerialType.INFO),
            SerialLogEntry(timeFormat.format(Date()), "[BOOT] Microcontroller: ${activeProject.board} [READY]", SerialType.INFO),
            SerialLogEntry(timeFormat.format(Date()), "RX < System Clock Calibrated (16.0 MHz)", SerialType.RX),
            SerialLogEntry(timeFormat.format(Date()), "RX < Sensor Bus I2C scanning... found 0x3C, 0x68", SerialType.RX)
        )
    }
    val logListState = rememberLazyListState()

    // Auto periodic telemetry simulation when streaming is active
    LaunchedEffect(isStreaming, activeProject) {
        var tick = 0
        while (isStreaming) {
            delay(2400)
            tick++
            val nowStr = timeFormat.format(Date())
            val randomVal = (180..650).random()
            val tempVal = String.format(Locale.US, "%.1f", 24.0 + (tick % 6) * 0.7)
            val log = when (tick % 4) {
                0 -> SerialLogEntry(nowStr, "RX < [${activeProject.board}] ADC_A0: $randomVal | VCC: 4.98V", SerialType.RX)
                1 -> SerialLogEntry(nowStr, "RX < Temp: ${tempVal}°C | Humidity: 62% [DHT OK]", SerialType.RX)
                2 -> SerialLogEntry(nowStr, "INFO < Heartbeat ACK: loop latency 2.4ms", SerialType.INFO)
                else -> SerialLogEntry(nowStr, "RX < Motor driver PWM Duty: ${((randomVal % 255))}/255", SerialType.RX)
            }
            serialLogs.add(log)
            if (serialLogs.size > 80) {
                serialLogs.removeAt(0)
            }
            logListState.animateScrollToItem(serialLogs.size - 1)
        }
    }

    // Code Snippets Preset Database
    val snippets = remember {
        listOf(
            ArduinoCodeSnippet(
                title = "Hardware PWM Fade & AnalogRead",
                category = "ANALOG / PWM",
                description = "Smooth LED brightness breathing and potentiometer reading.",
                code = """
// Hardware PWM Breathing LED & Analog Reading
const int pwmPin = 9;      // PWM output pin
const int potPin = A0;     // Potentiometer analog input

void setup() {
  Serial.begin(115200);
  pinMode(pwmPin, OUTPUT);
  Serial.println(F("[SYSTEM] PWM Breathing Ready"));
}

void loop() {
  int sensorValue = analogRead(potPin);
  float voltage = sensorValue * (5.0 / 1023.0);
  
  // Smooth sine wave brightness calculation
  for (int b = 0; b <= 255; b += 5) {
    analogWrite(pwmPin, b);
    delay(15);
  }
  for (int b = 255; b >= 0; b -= 5) {
    analogWrite(pwmPin, b);
    delay(15);
  }
  
  Serial.print(F("ADC_POT: "));
  Serial.print(sensorValue);
  Serial.print(F(" | Voltage: "));
  Serial.println(voltage);
}
""".trimIndent()
            ),
            ArduinoCodeSnippet(
                title = "HC-SR04 Ultrasonic Distance Sensor",
                category = "SENSORS",
                description = "High precision echo timing and distance calculation in centimeters.",
                code = """
// HC-SR04 Ultrasonic Rangefinder
const int trigPin = 11;
const int echoPin = 12;

void setup() {
  Serial.begin(115200);
  pinMode(trigPin, OUTPUT);
  pinMode(echoPin, INPUT);
  Serial.println(F("[HC-SR04] Ultrasonic Rangefinder Online"));
}

void loop() {
  // Clear trigger
  digitalWrite(trigPin, LOW);
  delayMicroseconds(2);
  
  // 10 microsecond pulse
  digitalWrite(trigPin, HIGH);
  delayMicroseconds(10);
  digitalWrite(trigPin, LOW);
  
  // Measure pulse duration
  long duration = pulseIn(echoPin, HIGH, 30000); // 30ms timeout
  float distanceCm = duration * 0.0343 / 2.0;
  
  Serial.print(F("RX < Distance: "));
  Serial.print(distanceCm);
  Serial.println(F(" cm"));
  delay(200);
}
""".trimIndent()
            ),
            ArduinoCodeSnippet(
                title = "ESP32 / WiFi JSON Telemetry Post",
                category = "IOT / WIRELESS",
                description = "Connects to Wi-Fi, builds structured JSON, and transmits telemetry.",
                code = """
// ESP32 WiFi Telemetry Node
#include <WiFi.h>

const char* ssid = "SHADOW_NET_2.4G";
const char* pass = "CyberShield#2026";

void setup() {
  Serial.begin(115200);
  WiFi.begin(ssid, pass);
  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.print(F("."));
  }
  Serial.println(F("\n[WIFI] Connected! IP: "));
  Serial.println(WiFi.localIP());
}

void loop() {
  // Transmit real-time telemetry frame
  Serial.println(F("TX > {\"node\":\"SHADOW_01\",\"status\":\"ONLINE\",\"rssi\":-48}"));
  delay(3000);
}
""".trimIndent()
            ),
            ArduinoCodeSnippet(
                title = "Active Project Code (${activeProject.board})",
                category = "ACTIVE PROJECT",
                description = activeProject.description,
                code = activeProject.code
            )
        )
    }

    var selectedSnippetIndex by remember { mutableIntStateOf(0) }
    val currentSnippet = snippets[selectedSnippetIndex]

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // ==========================================
        // 1. SIMULATED SERIAL MONITOR WINDOW
        // ==========================================
        CyberPanel(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Header Bar with Stream Status & Baud Selector
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(if (isStreaming) CyberSuccess else Color(0xFFFF5252))
                        )
                        Text(
                            text = "SIMULATED SERIAL MONITOR",
                            fontFamily = OrbitronFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp,
                            color = theme.primary
                        )
                    }

                    CyberStatusBadge(
                        label = "STREAM",
                        statusText = if (isStreaming) "LIVE RX/TX" else "PAUSED",
                        accentColor = if (isStreaming) CyberSuccess else Color(0xFFFF5252)
                    )
                }

                // Controls Row: Baud Rate Selector & Stream Controls
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "BAUD:",
                        fontFamily = OrbitronFontFamily,
                        fontSize = 9.sp,
                        color = CyberTextMuted
                    )

                    baudRates.forEach { rate ->
                        val isSelected = rate == selectedBaud
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(if (isSelected) theme.secondary.copy(alpha = 0.25f) else Color(0xFF0F172A))
                                .border(
                                    1.dp,
                                    if (isSelected) theme.secondary else Color(0xFF1E293B),
                                    RoundedCornerShape(4.dp)
                                )
                                .clickable {
                                    selectedBaud = rate
                                    serialLogs.add(
                                        SerialLogEntry(timeFormat.format(Date()), "--- BAUD RATE SWITCHED TO $rate ---", SerialType.INFO)
                                    )
                                    viewModel.audioSynth.playBeep(520f, 30, 0.06f)
                                }
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                                .testTag("baud_rate_$rate")
                        ) {
                            Text(
                                text = "$rate",
                                fontFamily = FontFamily.Monospace,
                                fontSize = 9.sp,
                                color = if (isSelected) theme.secondary else CyberTextSecondary
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(6.dp))

                    // Pause / Resume Toggle
                    NeonButton(
                        text = if (isStreaming) "PAUSE" else "RESUME",
                        onClick = {
                            isStreaming = !isStreaming
                            viewModel.audioSynth.playBeep(if (isStreaming) 700f else 450f, 40, 0.08f)
                        },
                        icon = if (isStreaming) Icons.Default.Stop else Icons.Default.PlayArrow,
                        accentColor = if (isStreaming) Color(0xFFFF5252) else CyberSuccess,
                        testTag = "toggle_serial_stream"
                    )

                    // Clear Console Button
                    NeonButton(
                        text = "CLEAR",
                        onClick = {
                            serialLogs.clear()
                            serialLogs.add(
                                SerialLogEntry(timeFormat.format(Date()), "--- CONSOLE CLEARED ---", SerialType.INFO)
                            )
                            viewModel.audioSynth.playBeep(350f, 40, 0.08f)
                        },
                        icon = Icons.Default.Delete,
                        testTag = "clear_serial_logs"
                    )
                }

                // Simulated Serial Console Terminal Box
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(Color(0xFF020408))
                        .border(1.dp, Color(0xFF1E293B), RoundedCornerShape(6.dp))
                        .padding(8.dp)
                ) {
                    LazyColumn(
                        state = logListState,
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(3.dp)
                    ) {
                        items(serialLogs) { log ->
                            val color = when (log.type) {
                                SerialType.RX -> Color(0xFF38BDF8) // Sky blue
                                SerialType.TX -> Color(0xFF4ADE80) // Neon green
                                SerialType.INFO -> Color(0xFFFFD700) // Amber
                                SerialType.WARN -> Color(0xFFFF5252) // Red
                            }
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text(
                                    text = log.timestamp,
                                    fontFamily = FontFamily.Monospace,
                                    fontSize = 9.sp,
                                    color = Color(0xFF64748B)
                                )
                                Text(
                                    text = log.text,
                                    fontFamily = FontFamily.Monospace,
                                    fontSize = 9.sp,
                                    color = color,
                                    lineHeight = 13.sp
                                )
                            }
                        }
                    }
                }

                // Command Send Bar
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = serialInput,
                        onValueChange = { serialInput = it },
                        placeholder = {
                            Text(
                                "Send serial cmd (e.g. AT, PING, READ_SENSOR, LED_ON)...",
                                fontSize = 10.sp,
                                color = CyberTextMuted
                            )
                        },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("serial_input_field"),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = theme.secondary,
                            unfocusedBorderColor = Color(0xFF1E293B),
                            focusedContainerColor = CyberDarkCard,
                            unfocusedContainerColor = CyberDarkCard,
                            focusedTextColor = CyberTextPrimary,
                            unfocusedTextColor = CyberTextPrimary
                        ),
                        singleLine = true
                    )

                    NeonButton(
                        text = "SEND",
                        onClick = {
                            if (serialInput.isNotBlank()) {
                                val cmd = serialInput.trim()
                                val now = timeFormat.format(Date())
                                serialLogs.add(SerialLogEntry(now, "TX > $cmd", SerialType.TX))

                                // Realistic simulated response
                                val responseText = when (cmd.uppercase()) {
                                    "PING" -> "RX < PONG (Latency: 1.2ms) [READY]"
                                    "AT" -> "RX < OK [FIRMWARE v2.4.1]"
                                    "READ_SENSOR", "READ" -> "RX < ADC_VAL=642 | TEMP=26.8C | HUMID=58%"
                                    "LED_ON" -> "RX < LED_PIN_13 set HIGH [OK]"
                                    "LED_OFF" -> "RX < LED_PIN_13 set LOW [OK]"
                                    "STATUS" -> "RX < SYS_OK: Memory Free=1742 bytes, CPU Load=14%"
                                    else -> "RX < ACK: Executed '$cmd' with result 0"
                                }
                                serialLogs.add(SerialLogEntry(now, responseText, SerialType.RX))
                                serialInput = ""
                                viewModel.audioSynth.playBeep(640f, 40, 0.08f)
                                coroutineScope.launch {
                                    logListState.animateScrollToItem(serialLogs.size - 1)
                                }
                            }
                        },
                        icon = Icons.Default.Send,
                        isActive = serialInput.isNotBlank(),
                        accentColor = theme.primary,
                        testTag = "send_serial_button"
                    )
                }

                // Quick Command Macro Chips
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    listOf("PING", "AT", "STATUS", "READ_SENSOR", "LED_ON", "LED_OFF").forEach { macro ->
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(Color(0xFF0B132B))
                                .border(1.dp, Color(0xFF1E293B), RoundedCornerShape(4.dp))
                                .clickable {
                                    serialInput = macro
                                    viewModel.audioSynth.playBeep(480f, 20, 0.05f)
                                }
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                                .testTag("serial_macro_$macro")
                        ) {
                            Text(
                                text = macro,
                                fontFamily = FontFamily.Monospace,
                                fontSize = 8.sp,
                                color = theme.secondary
                            )
                        }
                    }
                }
            }
        }

        // ==========================================
        // 2. ARDUINO CODE SNIPPET DISPLAY AREA
        // ==========================================
        CyberPanel(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Header & Copy Action
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "ARDUINO CODE SNIPPET AREA",
                            fontFamily = OrbitronFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp,
                            color = theme.secondary
                        )
                        Text(
                            text = currentSnippet.title.uppercase(),
                            fontFamily = OrbitronFontFamily,
                            fontSize = 9.sp,
                            color = Color(0xFFFFD700)
                        )
                    }

                    NeonButton(
                        text = "COPY SNIPPET",
                        onClick = {
                            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                            val clip = ClipData.newPlainText("Arduino Snippet", currentSnippet.code)
                            clipboard.setPrimaryClip(clip)
                            Toast.makeText(context, "Code snippet copied to clipboard!", Toast.LENGTH_SHORT).show()
                            viewModel.audioSynth.playDoubleChime()
                        },
                        icon = Icons.Default.ContentCopy,
                        accentColor = Color(0xFFFFD700),
                        testTag = "copy_arduino_snippet"
                    )
                }

                Text(
                    text = currentSnippet.description,
                    fontSize = 11.sp,
                    color = CyberTextSecondary
                )

                // Snippet Preset Selector Chips
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    snippets.forEachIndexed { idx, snip ->
                        val isSelected = idx == selectedSnippetIndex
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(if (isSelected) theme.secondary.copy(alpha = 0.25f) else CyberDarkSurface)
                                .border(
                                    1.dp,
                                    if (isSelected) theme.secondary else Color(0xFF1E293B),
                                    RoundedCornerShape(6.dp)
                                )
                                .clickable {
                                    selectedSnippetIndex = idx
                                    viewModel.audioSynth.playBeep(560f, 30, 0.06f)
                                }
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                                .testTag("snippet_selector_chip_$idx")
                        ) {
                            Text(
                                text = snip.category,
                                fontFamily = OrbitronFontFamily,
                                fontSize = 9.sp,
                                color = if (isSelected) theme.secondary else CyberTextMuted
                            )
                        }
                    }
                }

                // Monospace Syntax Code Display Area with Line Numbers
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 240.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(Color(0xFF010409))
                        .border(1.dp, Color(0xFF1E293B), RoundedCornerShape(6.dp))
                        .padding(10.dp)
                ) {
                    val lines = currentSnippet.code.lines()
                    LazyColumn(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        items(lines.size) { lineIdx ->
                            val line = lines[lineIdx]
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Text(
                                    text = String.format(Locale.US, "%2d", lineIdx + 1),
                                    fontFamily = FontFamily.Monospace,
                                    fontSize = 10.sp,
                                    color = Color(0xFF475569)
                                )
                                Text(
                                    text = line,
                                    fontFamily = FontFamily.Monospace,
                                    fontSize = 10.sp,
                                    color = when {
                                        line.trim().startsWith("//") -> Color(0xFF6B7280) // Comment grey
                                        line.contains("#include") || line.contains("void") || line.contains("const") -> theme.primary
                                        line.contains("Serial.print") || line.contains("analogWrite") || line.contains("digitalWrite") -> theme.secondary
                                        line.contains("F(") || line.contains("\"") -> Color(0xFFFFD700) // String amber
                                        else -> Color(0xFFE2E8F0) // Clean white
                                    },
                                    lineHeight = 15.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
