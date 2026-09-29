package com.example.data.model

data class ExamInfo(
    val id: String,
    val name: String,
    val category: ExamCategory,
    val conductingBody: String,
    val defaultSubjects: List<String>,
    val correctMarks: Float,
    val negativeMarks: Float,
    val durationMinutes: Int,
    val description: String
)

enum class ExamCategory(val displayName: String, val iconEmoji: String) {
    ENGINEERING("Engineering & Tech", "⚡"),
    CIVIL_SERVICES("Civil Services & State PSC", "🏛️"),
    DEFENCE("Defence & Armed Forces", "🎖️"),
    BANKING("Banking & Insurance", "🏦"),
    SSC_RAILWAYS("SSC & Railways", "🚆"),
    MEDICAL("Medical & Healthcare", "🩺"),
    LAW_MANAGEMENT("MBA & Law", "⚖️"),
    TEACHING("Teaching & Research", "📚")
}

object ExamCatalog {
    val allExams: List<ExamInfo> = listOf(
        // Engineering
        ExamInfo(
            id = "gate_ec",
            name = "GATE (Electronics & Communication - EC)",
            category = ExamCategory.ENGINEERING,
            conductingBody = "IISc / IITs",
            defaultSubjects = listOf("Analog Circuits", "Digital Circuits", "Signals & Systems", "Control Systems", "Electromagnetics", "Engineering Maths", "General Aptitude"),
            correctMarks = 2.0f,
            negativeMarks = 0.66f,
            durationMinutes = 180,
            description = "Graduate Aptitude Test in Engineering for M.Tech at IISc/IITs & PSU recruitment (ISRO, DRDO, BHEL, IOCL)."
        ),
        ExamInfo(
            id = "gate_ee",
            name = "GATE (Electrical Engineering - EE)",
            category = ExamCategory.ENGINEERING,
            conductingBody = "IISc / IITs",
            defaultSubjects = listOf("Electric Circuits", "Electrical Machines", "Power Systems", "Power Electronics", "Analog & Digital Electronics", "Control Systems", "Engineering Maths"),
            correctMarks = 2.0f,
            negativeMarks = 0.66f,
            durationMinutes = 180,
            description = "Premier exam for Electrical Engineers for PSU jobs (PowerGrid, NTPC, BHEL) and Masters degrees."
        ),
        ExamInfo(
            id = "gate_cs",
            name = "GATE (Computer Science & IT - CS)",
            category = ExamCategory.ENGINEERING,
            conductingBody = "IISc / IITs",
            defaultSubjects = listOf("Data Structures & Algorithms", "Operating Systems", "DBMS", "Computer Networks", "Theory of Computation", "Compiler Design", "Discrete Maths"),
            correctMarks = 2.0f,
            negativeMarks = 0.66f,
            durationMinutes = 180,
            description = "Gateway to IIT/IISc Master's programs and PSU scientist/officer roles."
        ),
        ExamInfo(
            id = "gate_me",
            name = "GATE (Mechanical Engineering - ME)",
            category = ExamCategory.ENGINEERING,
            conductingBody = "IISc / IITs",
            defaultSubjects = listOf("Thermodynamics", "Fluid Mechanics", "Strength of Materials", "Manufacturing Science", "Theory of Machines", "Engineering Maths"),
            correctMarks = 2.0f,
            negativeMarks = 0.66f,
            durationMinutes = 180,
            description = "Core mechanical PSU and Masters admission examination."
        ),
        ExamInfo(
            id = "gate_ce",
            name = "GATE (Civil Engineering - CE)",
            category = ExamCategory.ENGINEERING,
            conductingBody = "IISc / IITs",
            defaultSubjects = listOf("Structural Analysis", "Geotechnical Engineering", "Environmental Engineering", "Fluid Mechanics & Hydraulics", "Transportation Engineering", "Engineering Maths"),
            correctMarks = 2.0f,
            negativeMarks = 0.66f,
            durationMinutes = 180,
            description = "Civil engineering national examination for NHAI, CPWD, PSUs, and IIT admissions."
        ),
        ExamInfo(
            id = "gate_in",
            name = "GATE (Instrumentation Engineering - IN)",
            category = ExamCategory.ENGINEERING,
            conductingBody = "IISc / IITs",
            defaultSubjects = listOf("Transducers & Sensors", "Optical Instrumentation", "Analog Electronics", "Control Systems", "Signals & Systems", "Industrial Instrumentation"),
            correctMarks = 2.0f,
            negativeMarks = 0.66f,
            durationMinutes = 180,
            description = "Specialized engineering examination for process control, telemetry, and automation."
        ),
        ExamInfo(
            id = "ese_ies",
            name = "UPSC ESE / IES (Engineering Services)",
            category = ExamCategory.ENGINEERING,
            conductingBody = "UPSC",
            defaultSubjects = listOf("General Studies & Engineering Aptitude", "Technical Paper 1", "Technical Paper 2", "Conventional Engineering"),
            correctMarks = 2.0f,
            negativeMarks = 0.66f,
            durationMinutes = 180,
            description = "Prestigious Class-1 Gazetted officer positions in Indian Railways, CPWD, Military Engineer Services, etc."
        ),
        ExamInfo(
            id = "jee_adv",
            name = "JEE Advanced (IIT Entrance)",
            category = ExamCategory.ENGINEERING,
            conductingBody = "IIT Joint Admission Board",
            defaultSubjects = listOf("Physics (Mechanics & Electrodynamics)", "Chemistry (Organic & Physical)", "Mathematics (Calculus & Coordinate Geometry)"),
            correctMarks = 4.0f,
            negativeMarks = 1.0f,
            durationMinutes = 180,
            description = "The gold standard undergraduate engineering exam for admission to the 23 IITs."
        ),
        ExamInfo(
            id = "jee_main",
            name = "JEE Main (NITs, IIITs & CFTIs)",
            category = ExamCategory.ENGINEERING,
            conductingBody = "National Testing Agency (NTA)",
            defaultSubjects = listOf("Physics", "Chemistry", "Mathematics"),
            correctMarks = 4.0f,
            negativeMarks = 1.0f,
            durationMinutes = 180,
            description = "National entrance exam for NITs, IIITs, Centrally Funded Technical Institutes and JEE Advanced qualifier."
        ),
        ExamInfo(
            id = "isro_icrb",
            name = "ISRO ICRB Scientist/Engineer 'SC'",
            category = ExamCategory.ENGINEERING,
            conductingBody = "ISRO",
            defaultSubjects = listOf("Core Engineering Discipline", "Aptitude & Spatial Reasoning"),
            correctMarks = 3.0f,
            negativeMarks = 0.75f,
            durationMinutes = 120,
            description = "Direct recruitment exam for Scientist/Engineer 'SC' positions across ISRO centres."
        ),

        // Civil Services & Govt
        ExamInfo(
            id = "upsc_cse",
            name = "UPSC Civil Services (IAS / IPS / IFS)",
            category = ExamCategory.CIVIL_SERVICES,
            conductingBody = "Union Public Service Commission (UPSC)",
            defaultSubjects = listOf("Indian Polity & Governance", "Modern History & Freedom Struggle", "Indian Economy", "Geography & Environment", "Current Affairs", "CSAT Aptitude"),
            correctMarks = 2.0f,
            negativeMarks = 0.66f,
            durationMinutes = 120,
            description = "India's premier civil services exam selecting IAS, IPS, IFS, IRS, and central civil posts."
        ),
        ExamInfo(
            id = "uppsc_pcs",
            name = "UPPSC Combined State Services (PCS)",
            category = ExamCategory.CIVIL_SERVICES,
            conductingBody = "Uttar Pradesh PSC",
            defaultSubjects = listOf("General Studies Paper 1", "UP Special GS", "CSAT & General Hindi", "History & Polity"),
            correctMarks = 1.33f,
            negativeMarks = 0.44f,
            durationMinutes = 120,
            description = "Executive Sub-Divisional Magistrate (SDM), DSP, and commercial tax officer posts in Uttar Pradesh."
        ),
        ExamInfo(
            id = "bpsc_cce",
            name = "BPSC Combined Competitive Exam (CCE)",
            category = ExamCategory.CIVIL_SERVICES,
            conductingBody = "Bihar Public Service Commission",
            defaultSubjects = listOf("General Science", "History of Bihar & India", "Indian Polity & Economy", "National & International Events"),
            correctMarks = 1.0f,
            negativeMarks = 0.33f,
            durationMinutes = 120,
            description = "Top administrative and police recruitment exam for the state of Bihar."
        ),
        ExamInfo(
            id = "mppsc_sse",
            name = "MPPSC State Services Examination",
            category = ExamCategory.CIVIL_SERVICES,
            conductingBody = "Madhya Pradesh Public Service Commission",
            defaultSubjects = listOf("General Studies Paper 1 (MP GK)", "General Aptitude Test"),
            correctMarks = 2.0f,
            negativeMarks = 0.0f,
            durationMinutes = 120,
            description = "State administrative officer and deputy collector positions in Madhya Pradesh."
        ),

        // SSC & Railways
        ExamInfo(
            id = "ssc_cgl",
            name = "SSC CGL (Combined Graduate Level)",
            category = ExamCategory.SSC_RAILWAYS,
            conductingBody = "Staff Selection Commission",
            defaultSubjects = listOf("Quantitative Aptitude", "General Intelligence & Reasoning", "English Comprehension", "General Awareness"),
            correctMarks = 2.0f,
            negativeMarks = 0.5f,
            durationMinutes = 60,
            description = "Recruitment for Inspector of Income Tax, Central Excise, CBI Sub-Inspector, and Ministry ASOs."
        ),
        ExamInfo(
            id = "ssc_chsl",
            name = "SSC CHSL (10+2 Level)",
            category = ExamCategory.SSC_RAILWAYS,
            conductingBody = "Staff Selection Commission",
            defaultSubjects = listOf("Quantitative Aptitude", "General Intelligence", "General Awareness", "English Language"),
            correctMarks = 2.0f,
            negativeMarks = 0.5f,
            durationMinutes = 60,
            description = "Recruitment for Data Entry Operators (DEO), Lower Division Clerks (LDC), and Postal Assistants."
        ),
        ExamInfo(
            id = "rrb_ntpc",
            name = "RRB NTPC (Non-Technical Popular Categories)",
            category = ExamCategory.SSC_RAILWAYS,
            conductingBody = "Railway Recruitment Control Board",
            defaultSubjects = listOf("Mathematics", "General Intelligence & Reasoning", "General Awareness (Current Affairs, Science)"),
            correctMarks = 1.0f,
            negativeMarks = 0.33f,
            durationMinutes = 90,
            description = "Recruitment for Station Master, Goods Guard, Senior Clerk, and Commercial Apprentice in Indian Railways."
        ),

        // Banking & Insurance
        ExamInfo(
            id = "ibps_po",
            name = "IBPS PO (Probationary Officer)",
            category = ExamCategory.BANKING,
            conductingBody = "Institute of Banking Personnel Selection",
            defaultSubjects = listOf("Quantitative Aptitude / Data Interpretation", "Reasoning & Computer Aptitude", "English Language", "Banking Awareness & Economy"),
            correctMarks = 1.0f,
            negativeMarks = 0.25f,
            durationMinutes = 60,
            description = "Managerial career path across all major Public Sector Banks in India."
        ),
        ExamInfo(
            id = "sbi_po",
            name = "SBI PO (State Bank of India PO)",
            category = ExamCategory.BANKING,
            conductingBody = "State Bank of India",
            defaultSubjects = listOf("Data Analysis & Interpretation", "Reasoning Ability", "English Language", "General & Financial Awareness"),
            correctMarks = 1.0f,
            negativeMarks = 0.25f,
            durationMinutes = 60,
            description = "India's highest-regarded banking probationary officer examination with top career growth."
        ),
        ExamInfo(
            id = "rbi_grade_b",
            name = "RBI Grade 'B' Officer",
            category = ExamCategory.BANKING,
            conductingBody = "Reserve Bank of India",
            defaultSubjects = listOf("Economic and Social Issues", "Finance and Management", "General Awareness", "Quantitative Aptitude"),
            correctMarks = 1.0f,
            negativeMarks = 0.25f,
            durationMinutes = 120,
            description = "Elite regulatory central banking officer career with Reserve Bank of India."
        ),

        // Medical
        ExamInfo(
            id = "neet_ug",
            name = "NEET UG (Medical Entrance - MBBS/BDS)",
            category = ExamCategory.MEDICAL,
            conductingBody = "National Testing Agency (NTA)",
            defaultSubjects = listOf("Physics (Mechanics, Optics, Electrodynamics)", "Chemistry (Organic, Inorganic, Physical)", "Botany", "Zoology"),
            correctMarks = 4.0f,
            negativeMarks = 1.0f,
            durationMinutes = 200,
            description = "All-India entrance exam for MBBS and BDS admissions into AIIMS, JIPMER, and top medical colleges."
        ),
        ExamInfo(
            id = "neet_pg",
            name = "NEET PG (Postgraduate Medical - MD/MS)",
            category = ExamCategory.MEDICAL,
            conductingBody = "National Board of Examinations (NBE)",
            defaultSubjects = listOf("Clinical Medicine", "Surgery & Obstetrics", "Pathology & Pharmacology", "Anatomy & Physiology"),
            correctMarks = 4.0f,
            negativeMarks = 1.0f,
            durationMinutes = 210,
            description = "National level eligibility exam for admission to MD, MS, and PG Diploma medical courses."
        ),

        // Law & Management
        ExamInfo(
            id = "cat_mba",
            name = "CAT (Common Admission Test - IIMs)",
            category = ExamCategory.LAW_MANAGEMENT,
            conductingBody = "Indian Institutes of Management (IIMs)",
            defaultSubjects = listOf("Verbal Ability & Reading Comprehension (VARC)", "Data Interpretation & Logical Reasoning (DILR)", "Quantitative Aptitude (QA)"),
            correctMarks = 3.0f,
            negativeMarks = 1.0f,
            durationMinutes = 120,
            description = "Premier management entrance test for IIM Ahmedabad, Bangalore, Calcutta, and top B-schools."
        ),
        ExamInfo(
            id = "clat_ug",
            name = "CLAT (Common Law Admission Test - NLUs)",
            category = ExamCategory.LAW_MANAGEMENT,
            conductingBody = "Consortium of National Law Universities",
            defaultSubjects = listOf("Legal Reasoning", "Current Affairs & GK", "Logical Reasoning", "English Language", "Quantitative Techniques"),
            correctMarks = 1.0f,
            negativeMarks = 0.25f,
            durationMinutes = 120,
            description = "National level entrance test for admissions into the 24 National Law Universities (NLUs) in India."
        ),

        // Defence
        ExamInfo(
            id = "nda_na",
            name = "UPSC NDA & NA (National Defence Academy)",
            category = ExamCategory.DEFENCE,
            conductingBody = "Union Public Service Commission (UPSC)",
            defaultSubjects = listOf("Mathematics (Algebra, Trig, Calculus)", "General Ability Test (English & General Knowledge)"),
            correctMarks = 2.5f,
            negativeMarks = 0.83f,
            durationMinutes = 150,
            description = "Join Indian Army, Navy, and Air Force as a Commissioned Officer after 12th standard."
        ),
        ExamInfo(
            id = "cds_exam",
            name = "UPSC CDS (Combined Defence Services)",
            category = ExamCategory.DEFENCE,
            conductingBody = "Union Public Service Commission (UPSC)",
            defaultSubjects = listOf("Elementary Mathematics", "English", "General Knowledge & Current Affairs"),
            correctMarks = 1.0f,
            negativeMarks = 0.33f,
            durationMinutes = 120,
            description = "Graduate entry for Indian Military Academy (IMA), Officers Training Academy (OTA), and Air Force Academy."
        ),

        // Teaching
        ExamInfo(
            id = "ugc_net",
            name = "UGC NET / JRF (Assistant Professor & Research)",
            category = ExamCategory.TEACHING,
            conductingBody = "National Testing Agency (NTA)",
            defaultSubjects = listOf("Teaching & Research Aptitude (Paper 1)", "Specific Subject Domain (Paper 2)"),
            correctMarks = 2.0f,
            negativeMarks = 0.0f,
            durationMinutes = 180,
            description = "Determines eligibility for Assistant Professorship and Junior Research Fellowship (JRF) in Indian universities."
        )
    )

    fun findExamByName(name: String): ExamInfo {
        return allExams.firstOrNull { it.name.equals(name, ignoreCase = true) || name.contains(it.name, ignoreCase = true) }
            ?: allExams.first()
    }
}
