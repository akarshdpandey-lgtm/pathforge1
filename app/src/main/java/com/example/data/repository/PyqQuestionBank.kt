package com.example.data.repository

import com.example.data.model.QuizQuestion

object PyqQuestionBank {

    private val allQuestions: List<QuizQuestion> = listOf(
        // Analog Electronics - Op-Amps & Diodes (GATE / ESE / ISRO)
        QuizQuestion(
            id = 1,
            questionText = "For an ideal op-amp circuit with negative feedback, which of the following statements is TRUE regarding the virtual ground concept?",
            options = listOf(
                "A) Potential difference between inverting and non-inverting terminals is zero due to infinite open-loop gain",
                "B) The input current into the inverting terminal is infinite",
                "C) Virtual ground exists even when the op-amp is operating in deep saturation",
                "D) Output impedance of an ideal op-amp is infinite"
            ),
            correctIndex = 0,
            examYearTag = "GATE 2022 EC",
            explanation = "In an ideal op-amp with negative feedback in linear region, open-loop gain A -> ∞. Since Vout = A*(V+ - V-) is finite, (V+ - V-) must equal 0. Thus V- = V+ (virtual short/ground).",
            topic = "Analog Electronics"
        ),
        QuizQuestion(
            id = 2,
            questionText = "An inverting amplifier using an ideal op-amp has Rin = 10 kΩ and Rf = 100 kΩ. If a sinusoidal input of 0.2 V peak is applied with ±12 V supply, what is the closed-loop voltage gain and output peak voltage?",
            options = listOf(
                "A) Gain = -10, Vout,peak = 2.0 V",
                "B) Gain = +10, Vout,peak = 2.0 V",
                "C) Gain = -11, Vout,peak = 2.2 V",
                "D) Gain = -0.1, Vout,peak = 0.02 V"
            ),
            correctIndex = 0,
            examYearTag = "GATE 2020 EE",
            explanation = "For an inverting op-amp, Closed Loop Gain Av = -Rf / Rin = -100k / 10k = -10. Vout = -10 * 0.2 V = -2.0 V peak (within ±12V rail limits).",
            topic = "Analog Electronics"
        ),
        QuizQuestion(
            id = 3,
            questionText = "The slew rate of an operational amplifier is defined as the maximum rate of change of output voltage. An op-amp with a slew rate of 0.5 V/μs is used in a voltage follower. What is the full-power bandwidth for an undistorted output of 10 V peak?",
            options = listOf(
                "A) 7.96 kHz",
                "B) 15.9 kHz",
                "C) 31.8 kHz",
                "D) 50.0 kHz"
            ),
            correctIndex = 0,
            examYearTag = "GATE 2021 EC",
            explanation = "Slew Rate SR = 2π * fmax * Vpeak. Therefore, fmax = SR / (2π * Vpeak) = (0.5 * 10^6) / (2 * 3.1416 * 10) ≈ 7957.7 Hz ≈ 7.96 kHz.",
            topic = "Analog Electronics"
        ),
        QuizQuestion(
            id = 4,
            questionText = "In a BJT common-emitter amplifier circuit with unbypassed emitter resistor RE, what is the primary effect of RE on the amplifier's performance?",
            options = listOf(
                "A) Provides negative current-series feedback, stabilizing voltage gain and increasing input impedance",
                "B) Increases voltage gain significantly while reducing bandwidth",
                "C) Decreases the input resistance and causes thermal runaway",
                "D) Eliminates the Miller effect entirely"
            ),
            correctIndex = 0,
            examYearTag = "GATE 2023 EE",
            explanation = "An unbypassed emitter resistor RE introduces negative feedback (current-series). It stabilizes voltage gain (Av ≈ -Rc/RE), increases input impedance, and increases bandwidth at the cost of lower gain.",
            topic = "Analog Electronics"
        ),
        QuizQuestion(
            id = 5,
            questionText = "A silicon diode operates at room temperature (T = 300 K, thermal voltage VT = 26 mV). If the forward bias voltage is increased by 60 mV, by what factor does the diode forward current increase?",
            options = listOf(
                "A) Approximately 10 times",
                "B) Approximately 2 times",
                "C) Approximately 100 times",
                "D) Remains unchanged"
            ),
            correctIndex = 0,
            examYearTag = "GATE 2019 EC",
            explanation = "I_D ≈ I_0 * exp(V_D / (eta * V_T)). For eta ≈ 1, ΔI ratio = exp(ΔV / VT) = exp(60 mV / 26 mV) ≈ 10 times (approx 60mV per decade).",
            topic = "Analog Electronics"
        ),

        // Engineering Mathematics (GATE / ESE)
        QuizQuestion(
            id = 6,
            questionText = "For a 3x3 square matrix A, if the eigenvalues are 1, 2, and 3, what is the determinant and trace of matrix A?",
            options = listOf(
                "A) Determinant = 6, Trace = 6",
                "B) Determinant = 6, Trace = 14",
                "C) Determinant = 14, Trace = 6",
                "D) Determinant = 1, Trace = 3"
            ),
            correctIndex = 0,
            examYearTag = "GATE 2022 CS/EC",
            explanation = "Determinant equals the product of eigenvalues: 1 * 2 * 3 = 6. Trace equals the sum of eigenvalues: 1 + 2 + 3 = 6.",
            topic = "Engineering Mathematics"
        ),

        // Civil Services - Indian Polity & Constitution (UPSC CSE / State PSC)
        QuizQuestion(
            id = 7,
            questionText = "Under Article 32 of the Constitution of India, which of the following writs is issued by the Supreme Court to restrain a person from holding a public office to which he is not legally entitled?",
            options = listOf(
                "A) Quo-Warranto",
                "B) Mandamus",
                "C) Certiorari",
                "D) Habeas Corpus"
            ),
            correctIndex = 0,
            examYearTag = "UPSC CSE 2021",
            explanation = "Quo-Warranto (By what authority) is issued to prevent illegal usurpation of a public office by an unqualified person.",
            topic = "Indian Polity & Governance"
        ),
        QuizQuestion(
            id = 8,
            questionText = "With reference to the Parliament of India, what is the maximum permissible interval between two consecutive sessions of either House?",
            options = listOf(
                "A) Six months",
                "B) Three months",
                "C) Four months",
                "D) One year"
            ),
            correctIndex = 0,
            examYearTag = "UPSC CSE 2020",
            explanation = "Article 85(1) mandates that the President shall summon each House such that six months shall not intervene between its last sitting in one session and the date appointed for its first sitting in the next session.",
            topic = "Indian Polity & Governance"
        ),

        // Quantitative Aptitude (Banking - SBI/IBPS PO, SSC CGL, CAT)
        QuizQuestion(
            id = 9,
            questionText = "A sum of money doubles itself in 5 years at a certain rate of simple interest. In how many years will it become 4 times of itself at the same rate?",
            options = listOf(
                "A) 15 years",
                "B) 12 years",
                "C) 20 years",
                "D) 10 years"
            ),
            correctIndex = 0,
            examYearTag = "SSC CGL Tier-1 2022",
            explanation = "If P becomes 2P in 5 yrs, SI = P in 5 yrs (rate = 20%). For P to become 4P, required SI = 3P. Time = 3 * 5 = 15 years.",
            topic = "Quantitative Aptitude"
        ),
        QuizQuestion(
            id = 10,
            questionText = "Pipe A can fill a tank in 12 hours and Pipe B can empty it in 18 hours. If both pipes are opened simultaneously, in how many hours will the empty tank be completely filled?",
            options = listOf(
                "A) 36 hours",
                "B) 30 hours",
                "C) 24 hours",
                "D) 42 hours"
            ),
            correctIndex = 0,
            examYearTag = "IBPS PO 2022",
            explanation = "Net rate = (1/12 - 1/18) = (3 - 2)/36 = 1/36 tank per hour. Therefore, time required = 36 hours.",
            topic = "Quantitative Aptitude"
        ),

        // Physics & Mechanics (JEE Advanced / Main / NEET)
        QuizQuestion(
            id = 11,
            questionText = "A projectile is launched from ground level with speed u at angle θ to horizontal. At the highest point of its trajectory, what is its velocity and kinetic energy in terms of initial kinetic energy K?",
            options = listOf(
                "A) Velocity = u*cos(θ), Kinetic Energy = K*cos²(θ)",
                "B) Velocity = 0, Kinetic Energy = 0",
                "C) Velocity = u*sin(θ), Kinetic Energy = K*sin²(θ)",
                "D) Velocity = u, Kinetic Energy = K"
            ),
            correctIndex = 0,
            examYearTag = "JEE Main 2021",
            explanation = "At the highest point, vertical velocity vy = 0. Horizontal velocity vx = u*cos(θ) remains unchanged. Hence KE_top = 1/2 * m * (u*cosθ)² = K*cos²(θ).",
            topic = "Physics"
        ),

        // Network Theory (GATE / ESE)
        QuizQuestion(
            id = 12,
            questionText = "In a series RLC circuit at resonance frequency ω0, what is the circuit impedance and phase angle between voltage and current?",
            options = listOf(
                "A) Impedance Z = R (minimum), Phase angle = 0° (unity power factor)",
                "B) Impedance Z = ∞ (maximum), Phase angle = 90°",
                "C) Impedance Z = 0, Phase angle = 45°",
                "D) Impedance Z = √(R² + (ωL)²), Phase angle = -90°"
            ),
            correctIndex = 0,
            examYearTag = "GATE 2023 EE",
            explanation = "At series resonance, inductive reactance XL equals capacitive reactance XC (XL = XC). Net reactive impedance cancels out, leaving Z = R (purely resistive and minimum) and unity power factor (cos φ = 1, φ = 0°).",
            topic = "Network Theory"
        )
    )

    fun getQuestionsForTopic(topic: String, exam: String): List<QuizQuestion> {
        val topicLower = topic.lowercase().trim()
        val examLower = exam.lowercase().trim()

        val matching = allQuestions.filter {
            val qTopic = it.topic.lowercase()
            qTopic.contains(topicLower) || topicLower.contains(qTopic) ||
                    it.questionText.lowercase().contains(topicLower) ||
                    it.examYearTag.lowercase().contains(examLower)
        }
        val pool = if (matching.size >= 5) {
            matching.shuffled().take(5)
        } else {
            val remainder = (allQuestions - matching.toSet()).shuffled()
            (matching + remainder).take(5)
        }
        return pool.mapIndexed { index, q ->
            q.copy(
                id = index + 1,
                examYearTag = if (q.examYearTag.isNotBlank()) q.examYearTag else "$exam PYQ"
            )
        }
    }
}
