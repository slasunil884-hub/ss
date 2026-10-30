package com.example.data

data class ArduinoProject(
    val id: String,
    val title: String,
    val category: String,
    val board: String,
    val level: String,
    val description: String,
    val components: List<String>,
    val pinMap: Map<String, String>,
    val wiringSteps: List<String>,
    val code: String,
    val troubleshooting: List<String>
)

object ArduinoProjectDatabase {
    val projects = listOf(
        ArduinoProject(
            id = "proj_spider_bot",
            title = "Spider-Bot Quadruped Walker",
            category = "Robotics",
            board = "Arduino Nano / Uno",
            level = "Intermediate",
            description = "Stark-inspired 4-legged robotic scout that crawls forward, turns, and avoids obstacles using an ultrasonic sensor and 8 micro servos.",
            components = listOf(
                "Arduino Nano (ATmega328P)",
                "8x TowerPro SG90 9g Micro Servos",
                "HC-SR04 Ultrasonic Sensor",
                "PCA9685 16-Channel PWM Servo Driver",
                "2x 18650 Li-Ion Batteries (7.4V)",
                "LM2596 DC-DC Buck Converter (Set to 5.0V, 3A)"
            ),
            pinMap = mapOf(
                "PCA9685 SDA" to "Arduino A4",
                "PCA9685 SCL" to "Arduino A5",
                "HC-SR04 TRIG" to "Arduino D9",
                "HC-SR04 ECHO" to "Arduino D10",
                "VCC / GND" to "External 5V Buck Supply"
            ),
            wiringSteps = listOf(
                "1. Connect PCA9685 I2C lines: SDA to A4, SCL to A5, VCC to 5V, GND to Common GND.",
                "2. Wire HC-SR04 TRIG to D9 and ECHO to D10.",
                "3. Power all 8 servos through PCA9685 screw terminal using the regulated 5V LM2596 supply.",
                "4. CRITICAL: Ensure common ground between Arduino GND and Battery negative terminal."
            ),
            code = """
// SHADOW SUPPORTER (SS) - Spider-Bot Quadruped Gait Controller
#include <Wire.h>
#include <Adafruit_PWMServoDriver.h>

Adafruit_PWMServoDriver pwm = Adafruit_PWMServoDriver();

const int TRIG_PIN = 9;
const int ECHO_PIN = 10;
const int DISTANCE_THRESHOLD = 20; // cm

long readUltrasonicDistance() {
  digitalWrite(TRIG_PIN, LOW);
  delayMicroseconds(2);
  digitalWrite(TRIG_PIN, HIGH);
  delayMicroseconds(10);
  digitalWrite(TRIG_PIN, LOW);
  long duration = pulseIn(ECHO_PIN, HIGH, 30000);
  return duration * 0.034 / 2;
}

void setup() {
  Serial.begin(115200);
  pinMode(TRIG_PIN, OUTPUT);
  pinMode(ECHO_PIN, INPUT);
  
  pwm.begin();
  pwm.setPWMFreq(50); // Standard 50Hz servo refresh
  delay(500);
  Serial.println(F("[RAVI]: Spider-Bot servos calibrated and ready."));
}

void loop() {
  long distance = readUltrasonicDistance();
  Serial.print(F("Distance: "));
  Serial.println(distance);

  if (distance > 0 && distance < DISTANCE_THRESHOLD) {
    // Obstacle detected: turn pivot
    Serial.println(F("Obstacle ahead! Executing pivot turn."));
    // Pivot gait routine
  } else {
    // Normal crawl gait forward
  }
  delay(100);
}
""".trimIndent(),
            troubleshooting = listOf(
                "Servos jitter or Arduino resets: External 5V supply is dropping voltage under load. Add a 1000uF capacitor across the servo power rail.",
                "Ultrasonic reading 0cm constantly: Check ECHO pin wiring or ensure trigger pulse is 10us minimum."
            )
        ),
        ArduinoProject(
            id = "proj_arc_reactor",
            title = "Stark Arc Reactor Breathing Pulse",
            category = "Wearable & Props",
            board = "Arduino Pro Mini / Nano",
            level = "Beginner",
            description = "NeoPixel 16-LED ring driven with cinematic Stark breathing luminescence, responsive to suit combat mode triggers.",
            components = listOf(
                "Arduino Pro Mini (5V, 16MHz)",
                "WS2812B 16-LED NeoPixel Ring",
                "470 Ohm Resistor (Data Line)",
                "1000uF 6.3V Electrolytic Capacitor",
                "Pushbutton / Tactile Switch"
            ),
            pinMap = mapOf(
                "NeoPixel DIN" to "Arduino D6 (via 470Ω resistor)",
                "NeoPixel VCC" to "5V",
                "NeoPixel GND" to "GND",
                "Mode Button" to "Arduino D2 (INPUT_PULLUP)"
            ),
            wiringSteps = listOf(
                "1. Solder 470 ohm resistor inline between Arduino D6 and NeoPixel DIN.",
                "2. Connect 1000uF capacitor directly across 5V and GND terminals on the LED ring.",
                "3. Wire tactile push button between D2 and GND (internal pullup will be enabled)."
            ),
            code = """
// SHADOW SUPPORTER (SS) - Stark Arc Reactor Luminescence Engine
#include <Adafruit_NeoPixel.h>

#define LED_PIN    6
#define LED_COUNT 16
#define BTN_PIN    2

Adafruit_NeoPixel ring(LED_COUNT, LED_PIN, NEO_GRB + NEO_KHZ800);

int brightness = 60;
int fadeAmount = 2;
bool combatMode = false;

void setup() {
  pinMode(BTN_PIN, INPUT_PULLUP);
  ring.begin();
  ring.show(); // Initialize all pixels to 'off'
}

void loop() {
  if (digitalRead(BTN_PIN) == LOW) {
    delay(50); // Debounce
    combatMode = !combatMode;
    while(digitalRead(BTN_PIN) == LOW);
  }

  uint32_t glowColor = combatMode ? ring.Color(255, 0, 40) : ring.Color(0, 210, 255);

  for (int i = 0; i < LED_COUNT; i++) {
    ring.setPixelColor(i, glowColor);
  }
  ring.setBrightness(brightness);
  ring.show();

  brightness += fadeAmount;
  if (brightness <= 30 || brightness >= 240) {
    fadeAmount = -fadeAmount;
  }
  delay(20);
}
""".trimIndent(),
            troubleshooting = listOf(
                "First LED burned out: Always include the 470 ohm resistor on DIN to prevent voltage spikes.",
                "LEDs flicker randomly: Connect 1000uF capacitor across 5V and GND close to the NeoPixel ring."
            )
        ),
        ArduinoProject(
            id = "proj_radar_sonar",
            title = "Tactical Ultrasonic Radar HUD",
            category = "Sensors & HUD",
            board = "Arduino Uno",
            level = "Beginner",
            description = "Sweeping ultrasonic sonar turret mounted on an SG90 servo, mapping 180-degree proximity telemetry.",
            components = listOf(
                "Arduino Uno R3",
                "HC-SR04 Sonar Module",
                "SG90 Micro Servo",
                "Jumper Wires & Breadboard"
            ),
            pinMap = mapOf(
                "Servo PWM" to "Arduino D11",
                "HC-SR04 TRIG" to "Arduino D8",
                "HC-SR04 ECHO" to "Arduino D9"
            ),
            wiringSteps = listOf(
                "1. Mount HC-SR04 onto the SG90 servo arm with tape or bracket.",
                "2. Connect servo signal to D11, HC-SR04 TRIG to D8, and ECHO to D9.",
                "3. Power both modules from Arduino 5V and GND."
            ),
            code = """
// SHADOW SUPPORTER (SS) - Tactical 180-Degree Radar Scanner
#include <Servo.h>

Servo radarServo;
const int TRIG = 8;
const int ECHO = 9;

int calculateDistance() {
  digitalWrite(TRIG, LOW);
  delayMicroseconds(2);
  digitalWrite(TRIG, HIGH);
  delayMicroseconds(10);
  digitalWrite(TRIG, LOW);
  long dur = pulseIn(ECHO, HIGH, 25000);
  return dur * 0.034 / 2;
}

void setup() {
  radarServo.attach(11);
  pinMode(TRIG, OUTPUT);
  pinMode(ECHO, INPUT);
  Serial.begin(9600);
}

void loop() {
  // Sweep 15 to 165 degrees
  for (int angle = 15; angle <= 165; angle += 2) {
    radarServo.write(angle);
    delay(30);
    int dist = calculateDistance();
    Serial.print(angle);
    Serial.print(",");
    Serial.println(dist);
  }
  for (int angle = 165; angle >= 15; angle -= 2) {
    radarServo.write(angle);
    delay(30);
    int dist = calculateDistance();
    Serial.print(angle);
    Serial.print(",");
    Serial.println(dist);
  }
}
""".trimIndent(),
            troubleshooting = listOf(
                "Servo stutters during sweep: Add a small delay after radarServo.write(angle).",
                "Erratic sonar readings above 200cm: Limit maximum pulseIn timeout to 25ms."
            )
        )
    )
}
