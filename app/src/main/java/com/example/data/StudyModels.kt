package com.example.data

data class QuizQuestion(
    val id: String,
    val question: String,
    val questionSinhala: String = "",
    val options: List<String>,
    val correctIndex: Int,
    val explanation: String,
    val explanationSinhala: String = ""
)

data class StudyTopic(
    val title: String,
    val titleSinhala: String = "",
    val summary: String,
    val summarySinhala: String = "",
    val keyFormulas: List<String>,
    val examTips: String
)

data class StudySubject(
    val id: String,
    val name: String,
    val nameSinhala: String,
    val gradeRange: String,
    val iconName: String,
    val topics: List<StudyTopic>,
    val quiz: List<QuizQuestion>
)

data class StudyTask(
    val id: String = java.util.UUID.randomUUID().toString(),
    val title: String,
    val subject: String,
    val grade: String,
    val isCompleted: Boolean = false,
    val priority: String = "Normal" // High, Normal, Low
)

data class GroupMessage(
    val sender: String,
    val text: String,
    val timestamp: String = "Just now",
    val isAi: Boolean = false
)

data class StudyGroup(
    val id: String,
    val name: String,
    val grade: String,
    val joinKey: String,
    val description: String,
    val memberCount: Int,
    val messages: List<GroupMessage>
)

object StarkStudyDatabase {
    val subjects = listOf(
        StudySubject(
            id = "science",
            name = "SCIENCE (විද්‍යාව)",
            nameSinhala = "විද්‍යාව",
            gradeRange = "Grades 6 - 11 (O/L)",
            iconName = "atom",
            topics = listOf(
                StudyTopic(
                    title = "Photosynthesis & Respiration",
                    titleSinhala = "ප්‍රභාසංශ්ලේෂණය සහ සෛලීය ශ්වසනය",
                    summary = "Photosynthesis converts CO₂ and H₂O into glucose and oxygen in the presence of sunlight and chlorophyll. Respiration breaks down glucose to release energy (ATP).",
                    summarySinhala = "සූර්යාලෝකය සහ හරිතප්‍රද ඉදිරියේ CO₂ සහ H₂O යොදාගෙන ග්ලූකෝස් සහ ඔක්සිජන් නිපදවීම ප්‍රභාසංශ්ලේෂණයයි. ග්ලූකෝස් බිඳදමා ATP ශක්තිය මුදාහැරීම සෛලීය ශ්වසනයයි.",
                    keyFormulas = listOf(
                        "Photosynthesis: 6CO₂ + 6H₂O + Sunlight → C₆H₁₂O₆ + 6O₂",
                        "Respiration: C₆H₁₂O₆ + 6O₂ → 6CO₂ + 6H₂O + 38 ATP",
                        "Water photolysis releases atmospheric oxygen (H₂O split)",
                        "Dark Phase (Calvin Cycle) occurs in Chloroplast Stroma"
                    ),
                    examTips = "Remember that oxygen gas released during photosynthesis originates from water photolysis (H₂O), NOT carbon dioxide."
                ),
                StudyTopic(
                    title = "Newton's Laws & Mechanics",
                    titleSinhala = "නිව්ටන්ගේ නියම සහ යාන්ත්‍ර විද්‍යාව",
                    summary = "Newton's 1st Law (Inertia), 2nd Law (F = ma), and 3rd Law (Action-Reaction). Conservation of linear momentum.",
                    summarySinhala = "නිව්ටන්ගේ 1 වන නියමය (අවස්ථිතිය), 2 වන නියමය (F = ma), 3 වන නියමය (ක්‍රියාව සහ ප්‍රතික්‍රියාව).",
                    keyFormulas = listOf(
                        "Force: F = m × a (Newtons, kg·m/s²)",
                        "Momentum: p = m × v (kg·m/s)",
                        "Work Done: W = F × s (Joules)",
                        "Kinetic Energy: KE = ½mv²",
                        "Potential Energy: PE = mgh"
                    ),
                    examTips = "Always convert mass to kilograms (kg) and distance to meters (m) before computing Force or Work."
                ),
                StudyTopic(
                    title = "Electricity & Ohm's Law",
                    titleSinhala = "විද්‍යුතය සහ ඕම්ගේ නියමය",
                    summary = "Electric current is rate of charge flow. Ohm's law: V = IR at constant temperature.",
                    summarySinhala = "නියත උෂ්ණත්වයේදී සන්නායකයක් තුළින් ගලන ධාරාව එහි අග්‍ර අතර විභව අන්තරයට අනුලෝමව සමානුපාතික වේ (V = IR).",
                    keyFormulas = listOf(
                        "Ohm's Law: V = I × R (Volts, Amperes, Ohms)",
                        "Power: P = V × I = I²R = V²/R (Watts)",
                        "Resistors in Series: R_total = R₁ + R₂ + ...",
                        "Resistors in Parallel: 1/R_total = 1/R₁ + 1/R₂ + ..."
                    ),
                    examTips = "In parallel circuits, potential difference across each branch is identical."
                )
            ),
            quiz = listOf(
                QuizQuestion(
                    id = "sci_1",
                    question = "Which gas is released into the atmosphere during the light phase of photosynthesis?",
                    questionSinhala = "ප්‍රභාසංශ්ලේෂණයේ ආලෝක ප්‍රතික්‍රියාවේදී වායුගෝලයට මුදාහැරෙන වායුව කුමක්ද?",
                    options = listOf("Oxygen (O₂)", "Carbon Dioxide (CO₂)", "Nitrogen (N₂)", "Hydrogen Gas (H₂)"),
                    correctIndex = 0,
                    explanation = "Oxygen is released as a byproduct when water molecules are split (photolysis) during the light-dependent phase.",
                    explanationSinhala = "ආලෝක ප්‍රතික්‍රියාවේදී ජල අණු ප්‍රකාශ විච්ඡේදනය වීමෙන් ඔක්සිජන් වායුව නිදහස් වේ."
                ),
                QuizQuestion(
                    id = "sci_2",
                    question = "A force of 30 N acts on an object of mass 6 kg. What is the acceleration produced?",
                    questionSinhala = "ස්කන්ධය 6 kg වූ වස්තුවක් මත 30 N බලයක් යෙදූ විට ඇතිවන ත්වරණය කොපමණද?",
                    options = listOf("2.5 m/s²", "5.0 m/s²", "180 m/s²", "36 m/s²"),
                    correctIndex = 1,
                    explanation = "Using Newton's Second Law: a = F / m = 30 N / 6 kg = 5.0 m/s².",
                    explanationSinhala = "නිව්ටන්ගේ 2 වන නියමය අනුව: a = F / m = 30 N / 6 kg = 5.0 m/s²."
                )
            )
        ),
        StudySubject(
            id = "maths",
            name = "MATHEMATICS (ගණිතය)",
            nameSinhala = "ගණිතය",
            gradeRange = "Grades 6 - 11 (O/L)",
            iconName = "square_root",
            topics = listOf(
                StudyTopic(
                    title = "Quadratic Equations",
                    titleSinhala = "වර්ගජ සමීකරණ",
                    summary = "Standard form ax² + bx + c = 0 where a ≠ 0. Solvable by factoring, completing square, or quadratic formula.",
                    summarySinhala = "ax² + bx + c = 0 සම්මත ආකාරය. සාධක සෙවීමෙන්, වර්ගපූර්ණයෙන් හෝ සූත්‍රයෙන් විසඳිය හැක.",
                    keyFormulas = listOf(
                        "Quadratic Formula: x = [-b ± √(b² - 4ac)] / (2a)",
                        "Discriminant: Δ = b² - 4ac",
                        "Δ > 0: Two real distinct roots",
                        "Δ = 0: One real repeated root",
                        "Δ < 0: No real roots"
                    ),
                    examTips = "Check if Δ is a perfect square first. If so, factorizing directly is faster."
                ),
                StudyTopic(
                    title = "Pythagoras & Trigonometry",
                    titleSinhala = "පයිතගරස් ප්‍රමේයය සහ ත්‍රිකෝණමිතිය",
                    summary = "Right-angled triangle relationships and sine, cosine, tangent trigonometric ratios.",
                    summarySinhala = "සෘජුකෝණී ත්‍රිකෝණ සබඳතා සහ සයින්, කොස්, ටෑන් ත්‍රිකෝණමිතික අනුපාත.",
                    keyFormulas = listOf(
                        "Pythagoras Theorem: a² + b² = c² (c = hypotenuse)",
                        "sin(θ) = Opposite / Hypotenuse",
                        "cos(θ) = Adjacent / Hypotenuse",
                        "tan(θ) = Opposite / Adjacent"
                    ),
                    examTips = "SOH CAH TOA mnemonic helps recall trig ratios reliably."
                )
            ),
            quiz = listOf(
                QuizQuestion(
                    id = "math_1",
                    question = "What is the hypotenuse of a right triangle with perpendicular sides of 6 cm and 8 cm?",
                    questionSinhala = "කෝණික පාද 6 cm සහ 8 cm වන සෘජුකෝණී ත්‍රිකෝණයක කර්ණයේ දිග කොපමණද?",
                    options = listOf("10 cm", "14 cm", "12 cm", "9 cm"),
                    correctIndex = 0,
                    explanation = "By Pythagoras: c = √(6² + 8²) = √(36 + 64) = √100 = 10 cm.",
                    explanationSinhala = "පයිතගරස් ප්‍රමේයය අනුව: c = √(6² + 8²) = 10 cm."
                )
            )
        ),
        StudySubject(
            id = "ict",
            name = "ICT (තොරතුරු තාක්ෂණය)",
            nameSinhala = "තොරතුරු තාක්ෂණය",
            gradeRange = "Grades 6 - 13 (A/L & O/L)",
            iconName = "microchip",
            topics = listOf(
                StudyTopic(
                    title = "CPU Architecture & System Buses",
                    titleSinhala = "මධ්‍ය සැකසුම් ඒකකය (CPU) සහ පද්ධති බස්",
                    summary = "Von Neumann computer architecture: ALU (Math), CU (Control), Registers, and Memory.",
                    summarySinhala = "වොන් නියුමාන් ආකෘතිය: ALU (ගණිත සහ තාර්කික ඒකකය), CU (පාලන ඒකකය), රෙජිස්තර සහ මතකය.",
                    keyFormulas = listOf(
                        "ALU: Arithmetic Logic Unit",
                        "CU: Control Unit (Fetch-Decode-Execute cycle)",
                        "PC: Program Counter (Holds next instruction address)",
                        "Address Bus: Unidirectional (CPU → RAM)",
                        "Data Bus: Bidirectional"
                    ),
                    examTips = "Address Bus is unidirectional, while Data Bus is bidirectional."
                )
            ),
            quiz = listOf(
                QuizQuestion(
                    id = "ict_1",
                    question = "Which CPU component performs mathematical calculations and logical comparisons?",
                    questionSinhala = "ගණිතමය ගණනය කිරීම් සහ තාර්කික සංසන්දනයන් සිදුකරන CPU කොටස කුමක්ද?",
                    options = listOf("Control Unit (CU)", "Arithmetic Logic Unit (ALU)", "Cache Memory", "RAM"),
                    correctIndex = 1,
                    explanation = "The ALU (Arithmetic Logic Unit) executes all arithmetic and logic calculations.",
                    explanationSinhala = "ගණිත සහ තාර්කික ඒකකය (ALU) මඟින් ගණනය කිරීම් සිදුකරයි."
                )
            )
        )
    )

    val defaultTasks = listOf(
        StudyTask(title = "Revise Photosynthesis light/dark reaction formulas", subject = "Science", grade = "Grade 11", priority = "High"),
        StudyTask(title = "Solve 5 quadratic equation past paper problems", subject = "Maths", grade = "Grade 11", priority = "High"),
        StudyTask(title = "Draw Von Neumann CPU architecture block diagram", subject = "ICT", grade = "Grade 11", priority = "Normal"),
        StudyTask(title = "Review King Kashyapa Sigiriya water fountain notes", subject = "History", grade = "Grade 11", priority = "Normal")
    )

    val defaultStudyGroups = listOf(
        StudyGroup(
            id = "grp_ol_champions",
            name = "O/L Champions 2026 🇱🇰",
            grade = "Grade 10 - 11",
            joinKey = "SL-HERO-902",
            description = "Collaborative study group for Sri Lankan GCE O/L candidates. Daily past paper drills and AI tutor support.",
            memberCount = 28,
            messages = listOf(
                GroupMessage(sender = "Kavindu", text = "Guys, can someone explain quadratic formula discriminant Δ rules?"),
                GroupMessage(sender = "RAVI AI", text = "Sure Kavindu! If Δ = b² - 4ac > 0: two distinct real roots. If Δ = 0: one repeated root. If Δ < 0: no real roots.", isAi = true),
                GroupMessage(sender = "Thilini", text = "Thanks RAVI! That makes question 4 so clear.")
            )
        ),
        StudyGroup(
            id = "grp_robotics_club",
            name = "Stark Arduino & Robotics Lab",
            grade = "Grade 8 - 13",
            joinKey = "STARK-BOTS",
            description = "Building Arduino spider bots, sensors, and robotics inventions with AI circuit assistance.",
            memberCount = 19,
            messages = listOf(
                GroupMessage(sender = "Sahan", text = "My SG90 servo was jittering with 8 servos attached!"),
                GroupMessage(sender = "RAVI AI", text = "Sahan, the Arduino 5V pin cannot supply 8 servos simultaneously. Connect an external 5V 3A buck converter with common GND.", isAi = true),
                GroupMessage(sender = "Sahan", text = "Added the buck converter and 1000uF capacitor — works smoothly now!")
            )
        )
    )
}
