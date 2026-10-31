package com.example.data

data class FlashcardItem(
    val id: String,
    val grade: Int,
    val gradeRangeLabel: String,
    val subjectId: String,
    val subjectName: String,
    val topic: String,
    val frontPrompt: String,
    val frontPromptSinhala: String,
    val backAnswer: String,
    val backAnswerSinhala: String,
    val keyFormulaOrNote: String? = null,
    val difficulty: String = "Core", // "Core", "Exam Focus", "Advanced"
    var isMastered: Boolean = false
)

data class FlashcardModule(
    val id: String,
    val title: String,
    val titleSinhala: String,
    val subjectId: String,
    val subjectName: String,
    val grade: Int,
    val gradeBand: String, // "Primary (1-5)", "Junior (6-9)", "O/L (10-11)", "A/L (12-13)"
    val description: String,
    val descriptionSinhala: String,
    val cards: List<FlashcardItem>
)

object StarkFlashcardDatabase {

    val gradeBandOptions = listOf(
        "ALL" to "All Grades (1-13)",
        "PRIMARY" to "Primary (Grades 1-5)",
        "JUNIOR" to "Junior (Grades 6-9)",
        "OL" to "G.C.E. O/L (Grades 10-11)",
        "AL" to "G.C.E. A/L (Grades 12-13)"
    )

    val gradeDropdownList = listOf(
        0 to "All Grades (Grades 1 - 13)",
        1 to "Grade 1 (Primary - මූලික 1)",
        2 to "Grade 2 (Primary - මූලික 2)",
        3 to "Grade 3 (Primary - මූලික 3)",
        4 to "Grade 4 (Primary - මූලික 4)",
        5 to "Grade 5 (Scholarship Exam - 5 ශිෂ්‍යත්වය)",
        6 to "Grade 6 (Junior Secondary - කනිෂ්ඨ 6)",
        7 to "Grade 7 (Junior Secondary - කනිෂ්ඨ 7)",
        8 to "Grade 8 (Junior Secondary - කනිෂ්ඨ 8)",
        9 to "Grade 9 (Junior Secondary - කනිෂ්ඨ 9)",
        10 to "Grade 10 (G.C.E. O/L Foundation - සාමාන්‍ය පෙළ)",
        11 to "Grade 11 (G.C.E. O/L Exam - සාමාන්‍ය පෙළ)",
        12 to "Grade 12 (G.C.E. A/L Advanced - උසස් පෙළ)",
        13 to "Grade 13 (G.C.E. A/L Finals - උසස් පෙළ)"
    )

    val modules: List<FlashcardModule> = listOf(
        // 1. Grade 5 Scholarship Prep
        FlashcardModule(
            id = "mod_g5_scholarship",
            title = "Grade 5 Scholarship Essentials",
            titleSinhala = "5 ශිෂ්‍යත්ව පරිසරය සහ විද්‍යා මූලධර්ම",
            subjectId = "env",
            subjectName = "Environmental Studies (පරිසරය)",
            grade = 5,
            gradeBand = "PRIMARY",
            description = "High-frequency exam recall for Sri Lankan Grade 5 Scholarship candidates.",
            descriptionSinhala = "ශ්‍රී ලංකා 5 ශිෂ්‍යත්ව විභාගය සඳහා අත්‍යවශ්‍ය කරුණු සහ ආදර්ශ සංකල්ප.",
            cards = listOf(
                FlashcardItem(
                    id = "g5_1",
                    grade = 5,
                    gradeRangeLabel = "Grade 5 Scholarship",
                    subjectId = "env",
                    subjectName = "Environmental Studies",
                    topic = "Water Cycle (ජල චක්‍රය)",
                    frontPrompt = "What are the 4 main stages of the Earth's Water Cycle?",
                    frontPromptSinhala = "පෘථිවි ජල චක්‍රයේ ප්‍රධාන අදියර 4 මොනවාද?",
                    backAnswer = "1. Evaporation (Sun heats water)\n2. Transpiration (Plants release water vapor)\n3. Condensation (Clouds form)\n4. Precipitation (Rain/Dew falls)",
                    backAnswerSinhala = "1. වාෂ්පීභවනය (හිරු රශ්මියෙන් ජලය වාෂ්ප වීම)\n2. උත්ස්වේදනය (ශාක පත්‍ර මඟින් ජල වාෂ්ප පිටවීම)\n3. ඝනීභවනය (වලාකුළු සෑදීම)\n4. වර්ෂාපතනය (වැස්ස ලැබීම)",
                    keyFormulaOrNote = "Precipitation brings fresh water back to the soil & reservoirs.",
                    difficulty = "Exam Focus"
                ),
                FlashcardItem(
                    id = "g5_2",
                    grade = 5,
                    gradeRangeLabel = "Grade 5 Scholarship",
                    subjectId = "env",
                    subjectName = "Environmental Studies",
                    topic = "National Heritage (ජාතික සංකේත)",
                    frontPrompt = "What is the National Flower and National Tree of Sri Lanka?",
                    frontPromptSinhala = "ශ්‍රී ලංකාවේ ජාතික පුෂ්පය සහ ජාතික වෘක්ෂය මොනවාද?",
                    backAnswer = "• National Flower: Nil Mahanel (Blue Water Lily / Nymphaea nouchali)\n• National Tree: Na Tree (Ceylon Ironwood / Mesua ferrea)",
                    backAnswerSinhala = "• ජාතික පුෂ්පය: නිල් මහනෙල් මල\n• ජාතික වෘක්ෂය: නා ගස",
                    keyFormulaOrNote = "National Bird: Ceylon Junglefowl (වලි කුකුළා)",
                    difficulty = "Core"
                ),
                FlashcardItem(
                    id = "g5_3",
                    grade = 5,
                    gradeRangeLabel = "Grade 5 Scholarship",
                    subjectId = "env",
                    subjectName = "Environmental Studies",
                    topic = "Plant Adaptations (ශාක අනුවර්තන)",
                    frontPrompt = "Why do mangrove plants have breathing roots (Pneumatophores)?",
                    frontPromptSinhala = "කඩොලාන ශාක වල වායුධර මුල් (ශ්වසන මුල්) පිහිටා ඇත්තේ ඇයි?",
                    backAnswer = "Because mangrove soil is waterlogged and oxygen-deficient; pneumatophores grow upward into the air to absorb atmospheric oxygen.",
                    backAnswerSinhala = "කඩොලාන පරිසරයේ පස ජලයෙන් යටවී ඔක්සිජන් හිඟ බැවින්, වායුගෝලයෙන් ඔක්සිජන් ලබාගැනීමට මුල් ඉහළට වර්ධනය වේ.",
                    keyFormulaOrNote = "Example species: Kirala (Sonneratia caseolaris)",
                    difficulty = "Exam Focus"
                )
            )
        ),

        // 2. Grade 8 Junior Secondary - Science
        FlashcardModule(
            id = "mod_g8_science",
            title = "Grade 8 Junior Science & Matter",
            titleSinhala = "8 ශ්‍රේණිය පදාර්ථය සහ ශක්තිය",
            subjectId = "science",
            subjectName = "Science (විද්‍යාව)",
            grade = 8,
            gradeBand = "JUNIOR",
            description = "Atomic theory, states of matter, and basic energy transformations.",
            descriptionSinhala = "පරමාණුක ව්‍යුහය, පදාර්ථයේ අවස්ථා සහ මූලික ශක්ති පරිවර්තන.",
            cards = listOf(
                FlashcardItem(
                    id = "g8_1",
                    grade = 8,
                    gradeRangeLabel = "Grade 8 Junior",
                    subjectId = "science",
                    subjectName = "Science",
                    topic = "States of Matter",
                    frontPrompt = "Describe particle arrangement and energy in Solids, Liquids, and Gases.",
                    frontPromptSinhala = "ඝන, ද්‍රව සහ වායු වල අංශු සැකැස්ම සහ ශක්තිය පැහැදිලි කරන්න.",
                    backAnswer = "• Solid: Tightly packed, fixed lattice, vibrating in place, lowest kinetic energy.\n• Liquid: Close together, random flow, takes container shape.\n• Gas: Far apart, rapid random motion, fills entire volume, highest kinetic energy.",
                    backAnswerSinhala = "• ඝන: අංශු තදින් ඇහිරී ඇත, හැඩය ස්ථිරයි.\n• ද්‍රව: අංශු චලනය විය හැක, බඳුනේ හැඩය ගනී.\n• වායු: අංශු අතර විශාල හිඩැස් ඇත, නිදහසේ චලනය වේ.",
                    keyFormulaOrNote = "Latent heat is absorbed during phase change without temperature rise.",
                    difficulty = "Core"
                ),
                FlashcardItem(
                    id = "g8_2",
                    grade = 8,
                    gradeRangeLabel = "Grade 8 Junior",
                    subjectId = "science",
                    subjectName = "Science",
                    topic = "Heat Transfer Methods",
                    frontPrompt = "Name the 3 methods of heat transfer and their mediums.",
                    frontPromptSinhala = "තාපය සම්ප්‍රේෂණය වන ක්‍රම 3 සහ ඒවා සිදුවන මාධ්‍ය නම් කරන්න.",
                    backAnswer = "1. Conduction (Through solids via direct particle vibration)\n2. Convection (Through fluids/liquids/gases via convection currents)\n3. Radiation (Through vacuum via electromagnetic infrared waves)",
                    backAnswerSinhala = "1. සන්නයනය (ඝන ද්‍රව්‍ය හරහා)\n2. සංවහනය (ද්‍රව සහ වායු ධාරා මඟින්)\n3. විකිරණය (රික්තය හරහා විද්‍යුත් චුම්භක තරංග ලෙස)",
                    keyFormulaOrNote = "Solar radiation reaches Earth through vacuum space.",
                    difficulty = "Exam Focus"
                )
            )
        ),

        // 3. Grade 10 O/L History
        FlashcardModule(
            id = "mod_g10_history",
            title = "Grade 10 Sri Lankan History",
            titleSinhala = "10 ශ්‍රේණිය ලක්දිව වාරි ශිෂ්ටාචාරය සහ රාජධානි",
            subjectId = "history",
            subjectName = "History (ඉතිහාසය)",
            grade = 10,
            gradeBand = "OL",
            description = "Ancient hydraulic engineering, kingdom administration, and Sigiriya art.",
            descriptionSinhala = "පුරාණ වාරි තාක්ෂණය, රාජධානිය සහ සීගිරි කලා නිර්මාණ.",
            cards = listOf(
                FlashcardItem(
                    id = "g10_1",
                    grade = 10,
                    gradeRangeLabel = "Grade 10 O/L",
                    subjectId = "history",
                    subjectName = "History",
                    topic = "Hydraulic Architecture (වාරි තාක්ෂණය)",
                    frontPrompt = "What is the function of the Biso Kotuwa (Cistern Sluice)?",
                    frontPromptSinhala = "පුරාණ වැවේ බිසෝකොටුවේ ප්‍රධාන කාර්යය කුමක්ද?",
                    backAnswer = "The Biso Kotuwa dissipates enormous hydrostatic pressure inside the reservoir, allowing regulated, damage-free water discharge into the irrigation canals.",
                    backAnswerSinhala = "වැවේ ජල පීඩනය පාලනය කර වේල්ලට හානි නොවන පරිදි සොරොව්ව හරහා ඇළ මාර්ග වලට ජලය මුදාහැරීම.",
                    keyFormulaOrNote = "Invented by ancient Sri Lankan hydraulic engineers prior to the 3rd century BCE.",
                    difficulty = "Exam Focus"
                ),
                FlashcardItem(
                    id = "g10_2",
                    grade = 10,
                    gradeRangeLabel = "Grade 10 O/L",
                    subjectId = "history",
                    subjectName = "History",
                    topic = "King Parakramabahu I",
                    frontPrompt = "What famous quote was proclaimed by King Parakramabahu the Great?",
                    frontPromptSinhala = "මහා පරාක්‍රමබාහු රජු ප්‍රකාශ කළ සුප්‍රකට ප්‍රතිඥාව කුමක්ද?",
                    backAnswer = "\"Let not even a single drop of water that falls on this land go to the ocean without being useful to humankind.\"",
                    backAnswerSinhala = "\"අහසින් වැටෙන එකදු දිය බිඳක්වත් මිනිසාගේ ප්‍රයෝජනයට නොගෙන මුහුදට ගලා යාමට ඉඩ නොතැබිය යුතුය.\"",
                    keyFormulaOrNote = "Constructed the Sea of Parakrama (පරාක්‍රම සමුද්‍රය) by combining 5 reservoirs.",
                    difficulty = "Core"
                )
            )
        ),

        // 4. Grade 11 G.C.E. O/L Science
        FlashcardModule(
            id = "mod_g11_science",
            title = "Grade 11 O/L Science Master Deck",
            titleSinhala = "11 ශ්‍රේණිය සාමාන්‍ය පෙළ විද්‍යා විශිෂ්ටතා කාඩ්පත්",
            subjectId = "science",
            subjectName = "Science (විද්‍යාව)",
            grade = 11,
            gradeBand = "OL",
            description = "Critical physics, chemistry, and biology formulas tested in G.C.E. O/L exams.",
            descriptionSinhala = "සාමාන්‍ය පෙළ විභාගයේ අනිවාර්ය ප්‍රශ්න සඳහා භෞතික, රසායනික හා ජීව විද්‍යා සංකල්ප.",
            cards = listOf(
                FlashcardItem(
                    id = "g11_sci_1",
                    grade = 11,
                    gradeRangeLabel = "Grade 11 O/L",
                    subjectId = "science",
                    subjectName = "Science",
                    topic = "Photosynthesis & Respiration",
                    frontPrompt = "State the chemical equations for Photosynthesis and Aerobic Respiration.",
                    frontPromptSinhala = "ප්‍රභාසංශ්ලේෂණය සහ ස්වායු ශ්වසනය සඳහා තුලිත රසායනික සමීකරණ දක්වන්න.",
                    backAnswer = "• Photosynthesis:\n6CO₂ + 6H₂O + Sunlight → C₆H₁₂O₆ + 6O₂\n\n• Aerobic Respiration:\nC₆H₁₂O₆ + 6O₂ → 6CO₂ + 6H₂O + 38 ATP Energy",
                    backAnswerSinhala = "• ප්‍රභාසංශ්ලේෂණය:\n6CO₂ + 6H₂O + ආලෝකය → C₆H₁₂O₆ + 6O₂\n\n• ශ්වසනය:\nC₆H₁₂O₆ + 6O₂ → 6CO₂ + 6H₂O + 38 ATP ශක්තිය",
                    keyFormulaOrNote = "Oxygen released comes strictly from H₂O photolysis, not CO₂.",
                    difficulty = "Exam Focus"
                ),
                FlashcardItem(
                    id = "g11_sci_2",
                    grade = 11,
                    gradeRangeLabel = "Grade 11 O/L",
                    subjectId = "science",
                    subjectName = "Science",
                    topic = "Newton's Laws & Momentum",
                    frontPrompt = "What is Newton's Second Law and the Principle of Conservation of Momentum?",
                    frontPromptSinhala = "නිව්ටන්ගේ දෙවන නියමය සහ රේඛීය ගම්‍යතා සංස්ථිති නියමය කුමක්ද?",
                    backAnswer = "• Newton's 2nd Law: The rate of change of momentum is proportional to the resultant unbalanced external force (F = m × a).\n• Conservation of Momentum: In a closed system with no external forces, total momentum before collision = total momentum after collision (m₁u₁ + m₂u₂ = m₁v₁ + m₂v₂).",
                    backAnswerSinhala = "• 2 වන නියමය: F = ma (බලය = ස්කන්ධය × ත්වරණය)\n• ගම්‍යතා සංස්ථිතිය: බාහිර බලයක් නොයෙදෙන සංවෘත පද්ධතියක ගැටුමට පෙර මුළු ගම්‍යතාව = ගැටුමට පසු මුළු ගම්‍යතාව.",
                    keyFormulaOrNote = "SI Unit of Force is Newton (N = kg·m/s²).",
                    difficulty = "Exam Focus"
                ),
                FlashcardItem(
                    id = "g11_sci_3",
                    grade = 11,
                    gradeRangeLabel = "Grade 11 O/L",
                    subjectId = "science",
                    subjectName = "Science",
                    topic = "Electricity & Power",
                    frontPrompt = "State Ohm's Law and the 3 mathematical expressions for Electrical Power.",
                    frontPromptSinhala = "ඕම්ගේ නියමය සහ විද්‍යුත් ක්ෂමතාවය සෙවීමේ සූත්‍ර 3 දක්වන්න.",
                    backAnswer = "• Ohm's Law: V = I × R (at constant temperature)\n• Electrical Power:\n1. P = V × I\n2. P = I² × R\n3. P = V² / R",
                    backAnswerSinhala = "• ඕම්ගේ නියමය: V = IR\n• ක්ෂමතාව (Power):\n1. P = VI\n2. P = I²R\n3. P = V²/R (වොට් - Watts)",
                    keyFormulaOrNote = "1 kWh = 1000 W × 3600 s = 3.6 × 10⁶ Joules (1 electricity unit).",
                    difficulty = "Core"
                )
            )
        ),

        // 5. Grade 11 G.C.E. O/L Mathematics
        FlashcardModule(
            id = "mod_g11_maths",
            title = "Grade 11 O/L Mathematics Formulas",
            titleSinhala = "11 ශ්‍රේණිය සාමාන්‍ය පෙළ ගණිත සූත්‍ර සංග්‍රහය",
            subjectId = "maths",
            subjectName = "Mathematics (ගණිතය)",
            grade = 11,
            gradeBand = "OL",
            description = "Quadratic formula, trigonometry, circle theorems, and solid geometry.",
            descriptionSinhala = "වර්ගජ සමීකරණ, ත්‍රිකෝණමිතිය, වෘත්ත ප්‍රමේය සහ ඝන වස්තු පරිමා.",
            cards = listOf(
                FlashcardItem(
                    id = "g11_mat_1",
                    grade = 11,
                    gradeRangeLabel = "Grade 11 O/L",
                    subjectId = "maths",
                    subjectName = "Mathematics",
                    topic = "Quadratic Equations (වර්ගජ සමීකරණ)",
                    frontPrompt = "What is the Quadratic Formula and how do you test the nature of roots using Discriminant Δ?",
                    frontPromptSinhala = "වර්ගජ සූත්‍රය සහ විවේචකය (Δ) මඟින් මූල වල ස්වභාවය විමසන්නේ කෙසේද?",
                    backAnswer = "• Formula: x = [-b ± √(b² - 4ac)] / (2a)\n• Discriminant: Δ = b² - 4ac\n  - If Δ > 0: Two real distinct roots\n  - If Δ = 0: Two real equal (repeated) roots\n  - If Δ < 0: No real roots (imaginary)",
                    backAnswerSinhala = "• සූත්‍රය: x = [-b ± √(b² - 4ac)] / (2a)\n• විවේචකය Δ = b² - 4ac\n  - Δ > 0 නම්: තාත්වික අසමාන මූල 2කි.\n  - Δ = 0 නම්: තාත්වික සමාන මූල 2කි.\n  - Δ < 0 නම්: තාත්වික මූල නොපවතී.",
                    keyFormulaOrNote = "Ensure equation is in standard form ax² + bx + c = 0 before assigning a, b, c.",
                    difficulty = "Exam Focus"
                ),
                FlashcardItem(
                    id = "g11_mat_2",
                    grade = 11,
                    gradeRangeLabel = "Grade 11 O/L",
                    subjectId = "maths",
                    subjectName = "Mathematics",
                    topic = "Trigonometry & Pythagoras",
                    frontPrompt = "Write Pythagoras theorem and Sin, Cos, Tan formulas for a right triangle.",
                    frontPromptSinhala = "පයිතගරස් ප්‍රමේයය සහ සයින්, කොස්, ටෑන් සූත්‍ර සටහන් කරන්න.",
                    backAnswer = "• Pythagoras: a² + b² = c² (c = hypotenuse)\n• sin(θ) = Opposite / Hypotenuse\n• cos(θ) = Adjacent / Hypotenuse\n• tan(θ) = Opposite / Adjacent = sin(θ) / cos(θ)",
                    backAnswerSinhala = "• c² = a² + b² (c යනු කර්ණය)\n• sin(θ) = සම්මුඛ පාදය / කර්ණය\n• cos(θ) = බද්ධ පාදය / කර්ණය\n• tan(θ) = සම්මුඛ පාදය / බද්ධ පාදය",
                    keyFormulaOrNote = "Trig identities: sin²(θ) + cos²(θ) = 1.",
                    difficulty = "Core"
                ),
                FlashcardItem(
                    id = "g11_mat_3",
                    grade = 11,
                    gradeRangeLabel = "Grade 11 O/L",
                    subjectId = "maths",
                    subjectName = "Mathematics",
                    topic = "Circle Theorems (වෘත්ත ප්‍රමේය)",
                    frontPrompt = "State the relationship between angle at the center and angle at the circumference.",
                    frontPromptSinhala = "වෘත්තයක කේන්ද්‍රයේ කෝණය සහ පරිධියේ කෝණය අතර සම්බන්ධය කුමක්ද?",
                    backAnswer = "The angle subtended by an arc at the center of a circle is double the angle subtended by it at any point on the remaining circumference (Angle at center = 2 × Angle at circumference).",
                    backAnswerSinhala = "වෘත්තයක චාපයකින් කේන්ද්‍රයෙහි ආපාතනය කරන කෝණය, එම චාපයෙන්ම ඉතිරි පරිධිය මත ආපාතනය කරන කෝණය මෙන් දෙගුණයකි.",
                    keyFormulaOrNote = "Angle in a semicircle is always a right angle (90°).",
                    difficulty = "Exam Focus"
                )
            )
        ),

        // 6. Grade 11-13 ICT & Computing
        FlashcardModule(
            id = "mod_g11_ict",
            title = "Grade 11-13 ICT & Architecture",
            titleSinhala = "11-13 තොරතුරු තාක්ෂණය සහ පරිගණක ව්‍යුහය",
            subjectId = "ict",
            subjectName = "ICT (තොරතුරු තාක්ෂණය)",
            grade = 11,
            gradeBand = "OL",
            description = "Von Neumann model, memory hierarchy, logic gates, and network layers.",
            descriptionSinhala = "වොන් නියුමාන් ආකෘතිය, තර්ක ද්වාර සහ ජාල ආකෘති.",
            cards = listOf(
                FlashcardItem(
                    id = "g11_ict_1",
                    grade = 11,
                    gradeRangeLabel = "Grade 11 & A/L",
                    subjectId = "ict",
                    subjectName = "ICT",
                    topic = "Von Neumann Architecture",
                    frontPrompt = "List the main components of the CPU and their functions.",
                    frontPromptSinhala = "CPU හි ප්‍රධාන සංරචක සහ ඒවායේ කාර්යයන් මොනවාද?",
                    backAnswer = "1. ALU (Arithmetic Logic Unit): Executes arithmetic and logical operations.\n2. CU (Control Unit): Directs fetch-decode-execute cycle and controls buses.\n3. Registers: Ultra-fast temporary internal storage (PC, MAR, MDR, Accumulator).\n4. System Buses: Data bus (bidirectional), Address bus (unidirectional CPU→RAM), Control bus.",
                    backAnswerSinhala = "1. ALU: ගණිතමය සහ තාර්කික මෙහෙයුම් සිදුකරයි.\n2. CU: විධාන ලබාගැනීම, විකේතනය සහ ක්‍රියාත්මක කිරීම පාලනය කරයි.\n3. Registers: අභ්‍යන්තර ක්ෂණික මතක ඒකක (PC, MAR, MDR, ACC).\n4. පද්ධති බස්: Data bus (ද්වි-දිශා), Address bus (ඒක-දිශා).",
                    keyFormulaOrNote = "Cache memory sits between CPU registers and primary RAM to bridge speed gaps.",
                    difficulty = "Core"
                ),
                FlashcardItem(
                    id = "g11_ict_2",
                    grade = 11,
                    gradeRangeLabel = "Grade 11 & A/L",
                    subjectId = "ict",
                    subjectName = "ICT",
                    topic = "Logic Gates & Truth Tables",
                    frontPrompt = "What are the boolean operations of NAND and NOR, and why are they called Universal Gates?",
                    frontPromptSinhala = "NAND සහ NOR ද්වාර වල ක්‍රියාකාරිත්වය සහ ඒවා විශ්ව ද්වාර ලෙස හඳුන්වන්නේ ඇයි?",
                    backAnswer = "• NAND = NOT(AND): Output is 0 only when both inputs are 1.\n• NOR = NOT(OR): Output is 1 only when both inputs are 0.\n• Universal: Any boolean circuit (AND, OR, NOT, XOR) can be constructed solely using NAND gates or solely using NOR gates.",
                    backAnswerSinhala = "• NAND: ආදාන දෙකම 1 විට ප්‍රතිදානය 0 වේ (අනෙක් සෑම විටම 1).\n• NOR: ආදාන දෙකම 0 විට ප්‍රතිදානය 1 වේ.\n• විශ්ව ද්වාර: ඕනෑම තර්ක ද්වාරයක් NAND හෝ NOR පමණක් යොදාගෙන නිර්මාණය කළ හැකි බැවින්.",
                    keyFormulaOrNote = "De Morgan's Laws: (A · B)' = A' + B' and (A + B)' = A' · B'.",
                    difficulty = "Exam Focus"
                )
            )
        ),

        // 7. Grade 12-13 G.C.E. A/L Physics
        FlashcardModule(
            id = "mod_g13_physics",
            title = "Grade 12-13 A/L Physics Advanced",
            titleSinhala = "12-13 උසස් පෙළ භෞතික විද්‍යා උසස් සංකල්ප",
            subjectId = "physics",
            subjectName = "Physics (භෞතික විද්‍යාව)",
            grade = 13,
            gradeBand = "AL",
            description = "Wave optics, Doppler effect, Bernoulli's law, and Photoelectric emission.",
            descriptionSinhala = "තරංග ප්‍රකාශ විද්‍යාව, ඩොප්ලර් ආචරණය, බර්නූලි මූලධර්මය සහ ප්‍රකාශ විද්‍යුත් විමෝචනය.",
            cards = listOf(
                FlashcardItem(
                    id = "g13_phy_1",
                    grade = 13,
                    gradeRangeLabel = "Grade 13 A/L",
                    subjectId = "physics",
                    subjectName = "Physics",
                    topic = "Doppler Effect (ඩොප්ලර් ආචරණය)",
                    frontPrompt = "State the Doppler apparent frequency formula when source moves toward stationary observer.",
                    frontPromptSinhala = "ප්‍රභවය නිශ්චල නිරීක්ෂකයා දෙසට චලනය වන විට ඩොප්ලර් සංඛ්‍යාත සූත්‍රය කුමක්ද?",
                    backAnswer = "Apparent Frequency:\nf' = f₀ × [ v / (v - vₛ) ]\n\nWhere:\n• f₀ = Source fundamental frequency\n• v = Velocity of sound in air\n• vₛ = Velocity of approaching source\n• Because (v - vₛ) < v, perceived pitch f' increases.",
                    backAnswerSinhala = "දෘශ්‍ය සංඛ්‍යාතය f' = f₀ [ v / (v - vₛ) ]\nමෙහි f₀ යනු මූලික සංඛ්‍යාතය, v යනු වාතයේ ශබ්ද ප්‍රවේගය, vₛ යනු ප්‍රභවයේ ප්‍රවේගයයි. ප්‍රභවය ළඟා වන විට සංඛ්‍යාතය වැඩි වේ.",
                    keyFormulaOrNote = "When observer approaches stationary source: f' = f₀ × [ (v + vₒ) / v ].",
                    difficulty = "Advanced"
                ),
                FlashcardItem(
                    id = "g13_phy_2",
                    grade = 13,
                    gradeRangeLabel = "Grade 13 A/L",
                    subjectId = "physics",
                    subjectName = "Physics",
                    topic = "Bernoulli's Principle (බර්නූලි මූලධර්මය)",
                    frontPrompt = "State Bernoulli's equation for steady incompressible, non-viscous fluid flow.",
                    frontPromptSinhala = "අසම්පීඩ්‍ය, දුස්ස්‍රාවී නොවන තරල ප්‍රවාහයක් සඳහා බර්නූලි සමීකරණය කුමක්ද?",
                    backAnswer = "P + ½ρv² + ρgh = Constant\n\nWhere:\n• P = Static fluid pressure\n• ½ρv² = Dynamic pressure (kinetic energy per unit volume)\n• ρgh = Hydrostatic pressure (potential energy per unit volume)\n• ρ = Fluid density, v = Flow speed, h = Elevation",
                    backAnswerSinhala = "P + ½ρv² + ρgh = නියතයකි.\nස්ථිතික පීඩනය, චාලක පීඩනය සහ විභව පීඩනයෙහි එකතුව ප්‍රවාහය දිගේ නියතව පවතී.",
                    keyFormulaOrNote = "Application: Aerofoil lift, Venturi meter, and carburetor atomizers.",
                    difficulty = "Advanced"
                ),
                FlashcardItem(
                    id = "g13_phy_3",
                    grade = 13,
                    gradeRangeLabel = "Grade 13 A/L",
                    subjectId = "physics",
                    subjectName = "Physics",
                    topic = "Photoelectric Effect (ප්‍රකාශ විද්‍යුත් ආචරණය)",
                    frontPrompt = "State Einstein's Photoelectric Equation and define Work Function (Φ).",
                    frontPromptSinhala = "අයින්ස්ටයින්ගේ ප්‍රකාශ විද්‍යුත් සමීකරණය සහ කාර්ය ශ්‍රිතය (Φ) අර්ථ දක්වන්න.",
                    backAnswer = "• Equation: E = hf = Φ + KE_max = hf₀ + ½mv²_max\n• Work Function (Φ = hf₀): The minimum energy required to liberate an electron from the metal surface.\n• Threshold Frequency (f₀): Minimum photon frequency needed for photoelectric emission.",
                    backAnswerSinhala = "• සමීකරණය: hf = Φ + KE_max\n• කාර්ය ශ්‍රිතය (Φ): ලෝහ පෘෂ්ඨයෙන් ඉලෙක්ට්‍රෝනයක් නිදහස් කිරීමට අවශ්‍ය අවම ශක්තිය.\n• දේහලී සංඛ්‍යාතය (f₀): විමෝචනය සඳහා අවශ්‍ය අවම සංඛ්‍යාතය.",
                    keyFormulaOrNote = "Stopping potential: eV_s = KE_max = hf - Φ.",
                    difficulty = "Advanced"
                )
            )
        )
    )
}
