export const schemes = [
  {
    id: 1,
    name: "Pradhan Mantri Matru Vandana Yojana (PMMVY)",
    description: "A maternity benefit program that provides financial assistance to pregnant women and lactating mothers.",
    benefits: "₹5,000 in three installments for the first live birth.",
    eligibility: "Pregnant and lactating mothers aged 19 and above for their first live birth.",
    documents: "Aadhar Card, Bank Account, Pregnancy Registration",
    applicationProcess: "Apply through Anganwadi Centers or approved health facilities.",
    link: "https://pmmvy.wcd.gov.in/",
    eligibilityCheck: (data) => data.pregnancyStatus && data.hasAadhar && parseInt(data.age) >= 19
  },
  {
    id: 2,
    name: "Beti Bachao Beti Padhao",
    description: "A campaign to generate awareness and improve the efficiency of welfare services intended for girls.",
    benefits: "Educational support, awareness campaigns, improved access to education.",
    eligibility: "All girl children in India.",
    documents: "Birth Certificate, Aadhar Card",
    applicationProcess: "Implemented through district-level authorities.",
    link: "https://wcdhry.gov.in/schemes-for-women/beti-bachao-beti-padhao/",
    eligibilityCheck: (data) => true
  },
  {
    id: 3,
    name: "Sukanya Samriddhi Yojana",
    description: "A small deposit scheme for the girl child to provide financial security for education and marriage expenses.",
    benefits: "Higher interest rate (currently around 8.2% p.a.) with tax benefits under Section 80C.",
    eligibility: "Parents or legal guardians of girl children up to 10 years of age.",
    documents: "Birth Certificate, Identity proof of parents/guardian, Address proof",
    applicationProcess: "Apply at post offices or authorized banks.",
    link: "https://www.indiapost.gov.in/Financial/Pages/Content/Sukanya-Samriddhi-Account.aspx",
    eligibilityCheck: (data) => data.girlChild && parseInt(data.childAge) <= 10
  },
  {
    id: 4,
    name: "Working Women Hostel Scheme",
    description: "Provides safe and affordable accommodation for working women.",
    benefits: "Secure accommodation with daycare facilities for children.",
    eligibility: "Working women with monthly income not exceeding ₹50,000 in metro cities or ₹35,000 in other areas.",
    documents: "Employment proof, Income certificate, Identity proof",
    applicationProcess: "Apply through state government or directly to the hostels.",
    link: "https://wcd.nic.in/schemes/working-women-hostel",
    eligibilityCheck: (data) => data.workingStatus && parseInt(data.monthlyIncome) <= (data.metro ? 50000 : 35000)
  },
  {
    id: 5,
    name: "One Stop Centre Scheme",
    description: "Provides support and assistance to women affected by violence in public and private spaces.",
    benefits: "Emergency response, medical assistance, legal aid, psycho-social counseling, temporary shelter.",
    eligibility: "Women affected by violence, regardless of age, class, caste, or marital status.",
    documents: "Any available ID proof (not mandatory in emergency)",
    applicationProcess: "Direct walk-in to centers or through referrals from police/hospitals.",
    link: "https://sakhi.gov.in/",
    eligibilityCheck: (data) => true
  },
  {
    id: 6,
    name: "Mahila Shakti Kendra",
    description: "Empowers rural women through community participation and awareness generation.",
    benefits: "Skill development, employment, digital literacy, health and nutrition.",
    eligibility: "Rural women from all age groups.",
    documents: "Aadhar Card, Voter ID or any valid ID proof",
    applicationProcess: "Through village level centers and district level centers.",
    link: "https://wcd.nic.in/schemes/mahila-shakti-kendra",
    eligibilityCheck: (data) => data.ruralArea
  },
  {
    id: 7,
    name: "Support to Training and Employment Programme for Women (STEP)",
    description: "Provides skills to women that enhance employability in various sectors.",
    benefits: "Training, skills development, and access to employment and entrepreneurship.",
    eligibility: "Women above 16 years of age, especially from vulnerable groups.",
    documents: "Age proof, Address proof, BPL card (if applicable)",
    applicationProcess: "Through registered NGOs and community organizations.",
    link: "https://wcd.nic.in/schemes/support-training-and-employment-programme-women-step",
    eligibilityCheck: (data) => parseInt(data.age) >= 16
  },
  {
    id: 8,
    name: "Nari Shakti Puraskar",
    description: "Highest civilian honor for women in India, recognizing their contributions to society.",
    benefits: "Recognition, cash prize of ₹2 lakh, certificate.",
    eligibility: "Women who have made exceptional contributions to women's empowerment.",
    documents: "Nomination form with achievements documentation",
    applicationProcess: "Nominations through district authorities, state governments, or NGOs.",
    link: "https://wcd.nic.in/schemes/nari-shakti-puraskar",
    eligibilityCheck: (data) => data.achievements && data.contributionsToWomenEmpowerment
  },
  {
    id: 9,
    name: "SWADHAR Greh",
    description: "Rehabilitation scheme for women in difficult circumstances.",
    benefits: "Shelter, food, clothing, medical care, legal aid, and vocational training.",
    eligibility: "Women victims of difficult circumstances: homeless victims of domestic violence, mentally challenged women, trafficking victims, etc.",
    documents: "Police report (if applicable), Medical reports (if applicable), Reference from NGO/government agency",
    applicationProcess: "Through police, NGOs, or direct approach to SWADHAR homes.",
    link: "https://wcd.nic.in/schemes/swadhar-greh-scheme-women-difficult-circumstances",
    eligibilityCheck: (data) => data.difficultCircumstances || data.homelessStatus || data.domesticViolenceVictim
  },
  {
    id: 10,
    name: "Ujjawala Scheme",
    description: "A comprehensive scheme for prevention of trafficking and rescue, rehabilitation of women and child victims of trafficking.",
    benefits: "Rescue services, rehabilitation, reintegration, and repatriation of victims of trafficking.",
    eligibility: "Women and children who are victims of trafficking for commercial sexual exploitation.",
    documents: "Police report or referral from recognized agency",
    applicationProcess: "Through NGOs, police, or anti-trafficking units.",
    link: "https://wcd.nic.in/schemes/ujjawala-comprehensive-scheme-prevention-trafficking-and-rescue-rehabilitation-and-re",
    eligibilityCheck: (data) => data.traffickingVictim
  },
  {
    id: 11,
    name: "Rashtriya Mahila Kosh (RMK)",
    description: "Provides micro-credit to poor women for income-generating activities.",
    benefits: "Micro-loans at reasonable rates, entrepreneurship development.",
    eligibility: "Poor women with preference to women from SC/ST/OBC, women with disabilities, and women-headed households.",
    documents: "Identity proof, Address proof, Income certificate",
    applicationProcess: "Through NGO-MFIs, Women Self Help Groups, or directly.",
    link: "https://socialjustice.gov.in/common/1271#National-Institutes",
    eligibilityCheck: (data) => data.lowIncome || data.belongsToSCST || data.disability
  },
  {
    id: 12,
    name: "Mahila E-Haat",
    description: "Online marketing platform for women entrepreneurs.",
    benefits: "Direct marketing platform, wider reach for products, training and support.",
    eligibility: "Women entrepreneurs, Self Help Groups, NGOs.",
    documents: "Identity proof, Business registration (if applicable), Product details",
    applicationProcess: "Online registration through the Mahila E-Haat portal.",
    link: "https://socialjustice.gov.in/common/1271#National-Institutes",
    eligibilityCheck: (data) => data.entrepreneur || data.partOfSHG
  },
  {
    id: 13,
    name: "Women Helpline Scheme",
    description: "24-hour emergency response service for women affected by violence.",
    benefits: "Emergency assistance, information about women-related government schemes, legal aid.",
    eligibility: "All women in distress or facing violence.",
    documents: "No documents required for immediate assistance",
    applicationProcess: "Dial 181 or state-specific women helpline numbers.",
    link: "https://wcd.nic.in/schemes/women-helpline-scheme-0",
    eligibilityCheck: (data) => true
  },
  {
    id: 14,
    name: "NARI Portal",
    description: "Information portal that provides information on government schemes and initiatives for women.",
    benefits: "Easy access to information on schemes, services, and support systems.",
    eligibility: "All women seeking information about government schemes.",
    documents: "Not applicable (information service)",
    applicationProcess: "Access online through the NARI portal.",
    link: "http://nari.nic.in/",
    eligibilityCheck: (data) => true
  },
  {
    id: 15,
    name: "Mahila Police Volunteers",
    description: "Links between police and community to help women in distress.",
    benefits: "Support in filing complaints, awareness about rights, assistance in accessing legal aid.",
    eligibility: "Women in need of assistance related to violence or harassment.",
    documents: "Any available ID proof (for formal complaints)",
    applicationProcess: "Contact local police station or women cell.",
    link: "https://wcd.nic.in/schemes/mahila-police-volunteers",
    eligibilityCheck: (data) => true
  },
  {
    id: 16,
    name: "Janani Suraksha Yojana (JSY)",
    description: "Safe motherhood intervention to reduce maternal and neonatal mortality.",
    benefits: "Cash assistance for delivery and post-delivery care (₹700 in rural areas, ₹600 in urban areas for LPS states).",
    eligibility: "Pregnant women, especially from BPL families.",
    documents: "Pregnancy registration card, BPL card (if applicable), Aadhar card",
    applicationProcess: "Through ASHA workers, ANMs, or government health facilities.",
    link: "https://socialjustice.gov.in/common/1271#National-Institutes",
    eligibilityCheck: (data) => data.pregnancyStatus
  },
  {
    id: 17,
    name: "Indira Gandhi Matritva Sahyog Yojana (IGMSY)",
    description: "Contributes to better environment for health and nutrition of pregnant and nursing mothers.",
    benefits: "Cash incentive of ₹6,000 for improved health and nutrition.",
    eligibility: "Pregnant and lactating women of 19 years of age or above for up to two live births.",
    documents: "Aadhar Card, Bank Account, Pregnancy Registration",
    applicationProcess: "Through Anganwadi workers.",
    link: "https://socialjustice.gov.in/common/1271#National-Institutes",
    eligibilityCheck: (data) => data.pregnancyStatus && data.hasAadhar && parseInt(data.age) >= 19
  },
  {
    id: 18,
    name: "SABLA (Rajiv Gandhi Scheme for Empowerment of Adolescent Girls)",
    description: "Empowers adolescent girls with life skills, health, and nutrition education.",
    benefits: "Nutrition, iron and folic acid supplementation, health check-up, skill training.",
    eligibility: "Adolescent girls aged 11-18 years.",
    documents: "Age proof, School enrollment certificate (if applicable)",
    applicationProcess: "Through Anganwadi Centers.",
    link: "https://socialjustice.gov.in/common/1271#National-Institutes",
    eligibilityCheck: (data) => parseInt(data.age) >= 11 && parseInt(data.age) <= 18
  },
  {
    id: 19,
    name: "National Creche Scheme",
    description: "Provides daycare facilities for children of working mothers.",
    benefits: "Daycare services, nutrition, health monitoring for children.",
    eligibility: "Children of working mothers aged 6 months to 6 years.",
    documents: "Child's birth certificate, Mother's employment proof, Income certificate",
    applicationProcess: "Through local anganwadi or municipality office.",
    link: "https://wcd.nic.in/schemes/national-creche-scheme",
    eligibilityCheck: (data) => data.workingStatus && data.hasChildrenUnder6
  },
  {
    id: 20,
    name: "Gender Budgeting Scheme",
    description: "Incorporates gender perspective in budgetary policies and programs.",
    benefits: "Ensures adequate resource allocation for women's development.",
    eligibility: "Not an individual-level scheme (policy-level intervention)",
    documents: "Not applicable",
    applicationProcess: "Not applicable (policy initiative)",
    link: "https://wcd.nic.in/schemes/gender-budgeting-scheme",
    eligibilityCheck: (data) => false
  }
];

export const additionalSchemes = [
  {
    id: 21,
    name: "Nirbhaya Fund Scheme",
    description: "Supports initiatives for women's safety and security in India.",
    benefits: "Emergency response system, women help desks, safe city projects.",
    eligibility: "All women in need of emergency assistance.",
    documents: "Any valid ID proof in emergency situations",
    applicationProcess: "Through police stations, women helplines, or One Stop Centres.",
    link: "https://socialjustice.gov.in/common/1271#National-Institutes",
    eligibilityCheck: (data) => true
  },
  {
    id: 22,
    name: "Stree Shakti Puraskar",
    description: "Recognizes the efforts of individual women who have worked for the upliftment of other women in society.",
    benefits: "Recognition and cash prize of ₹1 lakh.",
    eligibility: "Women who have made exceptional contributions to women's empowerment.",
    documents: "Nomination form with documentation of achievements",
    applicationProcess: "Nominations through district authorities or state governments.",
    link: "https://wcd.nic.in/schemes/stree-shakti-puraskar",
    eligibilityCheck: (data) => data.achievements && data.contributionsToWomenEmpowerment
  },
  {
    id: 23,
    name: "Women Technology Park",
    description: "Promotes women's participation in science and technology for sustainable development.",
    benefits: "Access to technology, training, and product development facilities.",
    eligibility: "Women entrepreneurs and SHGs involved in technology-based enterprises.",
    documents: "Identity proof, Business plan, Group registration (for SHGs)",
    applicationProcess: "Through state science and technology departments.",
    link: "https://dst.gov.in/women-scientist-schemes",
    eligibilityCheck: (data) => data.entrepreneur || data.partOfSHG
  },
  {
    id: 24,
    name: "STEM (Science, Technology, Engineering, Mathematics) for Women",
    description: "Encourages women to pursue education and careers in STEM fields.",
    benefits: "Scholarships, mentoring, career guidance.",
    eligibility: "Female students pursuing education in STEM fields.",
    documents: "Academic records, Identity proof, Admission proof",
    applicationProcess: "Through educational institutions or DST portal.",
    link: "https://dst.gov.in/women-scientist-schemes",
    eligibilityCheck: (data) => data.femaleStudent && data.stemEducation
  },
  {
    id: 25,
    name: "Stand Up India",
    description: "Facilitates bank loans for SC/ST women and women entrepreneurs.",
    benefits: "Bank loans between ₹10 lakh and ₹1 crore for setting up greenfield enterprises.",
    eligibility: "Women entrepreneurs starting new enterprises.",
    documents: "Identity proof, Address proof, Business plan",
    applicationProcess: "Apply through banks or online portal.",
    link: "https://www.standupmitra.in/",
    eligibilityCheck: (data) => data.entrepreneur || data.belongsToSCST
  },
  {
    id: 26,
    name: "Mahila Samridhi Yojana",
    description: "Encourages women to save and promotes their economic development.",
    benefits: "Subsidized interest rates and training for starting small businesses.",
    eligibility: "Women from low-income households, especially rural areas.",
    documents: "Identity proof, Address proof, Income certificate",
    applicationProcess: "Through rural development offices or banks.",
    link: "https://rural.nic.in/scheme-websites",
    eligibilityCheck: (data) => data.lowIncome || data.ruralArea
  },
  {
    id: 27,
    name: "Deen Dayal Upadhyaya Antyodaya Yojana (DAY-NRLM)",
    description: "Reduces poverty through women's Self Help Groups.",
    benefits: "Financial assistance, interest subvention, skill development.",
    eligibility: "Rural women, especially from poor households.",
    documents: "Identity proof, BPL card (if applicable), Bank account",
    applicationProcess: "Through block or district rural development offices.",
    link: "https://aajeevika.gov.in/",
    eligibilityCheck: (data) => data.ruralArea || data.lowIncome
  },
  {
    id: 28,
    name: "Pradhan Mantri Kaushal Vikas Yojana (PMKVY) - Special provisions for women",
    description: "Skill development initiative with special focus on women participants.",
    benefits: "Free skill training, certification, monetary reward on assessment.",
    eligibility: "Women seeking skill development, with preference to rural, marginalized groups.",
    documents: "Identity proof, Age proof, Educational qualifications",
    applicationProcess: "Through nearest PMKVY Training Center.",
    link: "https://pmkvyofficial.org/",
    eligibilityCheck: (data) => true
  },
  {
    id: 29,
    name: "Udyogini Scheme",
    description: "Promotes entrepreneurship among women by providing loans at subsidized rates.",
    benefits: "Subsidized loans up to ₹1 lakh and training for entrepreneurship development.",
    eligibility: "Women with family income below ₹1.5 lakh per annum.",
    documents: "Identity proof, Income certificate, Project proposal",
    applicationProcess: "Through District Industries Centers or Women Development Corporations.",
    link: "https://womeneconomicforum.com/web/pages/en/udyogini-scheme",
    eligibilityCheck: (data) => parseInt(data.familyIncome) <= 150000
  },
  {
    id: 30,
    name: "Bhartiya Mahila Bank Business Loan",
    description: "Specialized bank loans for women entrepreneurs (now merged with SBI).",
    benefits: "Loans up to ₹20 crore for manufacturing enterprises and ₹10 crore for service sector.",
    eligibility: "Women entrepreneurs with viable business plans.",
    documents: "Identity proof, Business plan, Property documents (if applicable)",
    applicationProcess: "Apply through SBI branches.",
    link: "https://www.sbi.co.in/web/business/sme/sme-loans/women-entrepreneurs",
    eligibilityCheck: (data) => data.entrepreneur
  },
  {
    id: 31,
    name: "Annapurna Scheme",
    description: "Financial assistance to women in food catering business.",
    benefits: "Loans up to ₹50,000 with subsidized interest rates.",
    eligibility: "Women entrepreneurs in food catering business.",
    documents: "Identity proof, Business proposal, Two guarantors",
    applicationProcess: "Through nationalized banks.",
    link: "https://www.sidbi.in/en/products",
    eligibilityCheck: (data) => data.entrepreneur && data.foodCateringBusiness
  },
  {
    id: 32,
    name: "Cent Kalyani Scheme",
    description: "Central Bank of India's scheme for women entrepreneurs in micro and small enterprises.",
    benefits: "Loans up to ₹100 lakhs for manufacturing and ₹25 lakhs for service sector with no collateral.",
    eligibility: "Women entrepreneurs aged 18-60 years setting up new ventures.",
    documents: "Identity proof, Business plan, Age proof",
    applicationProcess: "Apply through Central Bank of India branches.",
    link: "https://www.centralbankofindia.co.in/en/cent-kalyani",
    eligibilityCheck: (data) => data.entrepreneur && parseInt(data.age) >= 18 && parseInt(data.age) <= 60
  },
  {
    id: 33,
    name: "Mudra Yojana (Special provisions for women)",
    description: "Provides loans to women entrepreneurs for non-corporate, non-farm small businesses.",
    benefits: "Loans up to ₹10 lakhs under three categories: Shishu, Kishore, and Tarun.",
    eligibility: "Women entrepreneurs in non-farm sector.",
    documents: "Identity proof, Business plan, Bank account",
    applicationProcess: "Apply through banks, MFIs, or NBFCs.",
    link: "https://www.mudra.org.in/",
    eligibilityCheck: (data) => data.entrepreneur || data.smallBusiness
  },
  {
    id: 34,
    name: "Dena Shakti Scheme",
    description: "Provides financial assistance to women entrepreneurs in various sectors.",
    benefits: "Loans with concession of 0.25% on interest rate.",
    eligibility: "Women entrepreneurs in agriculture, manufacturing, micro-credit, retail, etc.",
    documents: "Identity proof, Business plan, Property documents (if applicable)",
    applicationProcess: "Apply through Bank of Baroda branches (formerly Dena Bank).",
    link: "https://www.bankofbaroda.in/business-banking/sme-banking",
    eligibilityCheck: (data) => data.entrepreneur
  },
  {
    id: 35,
    name: "Priyadarshini Yojana",
    description: "Promotes women's empowerment through sustainable livelihood opportunities.",
    benefits: "Credit-linked grant assistance for income-generating activities.",
    eligibility: "Women in rural areas, especially from vulnerable communities.",
    documents: "Identity proof, BPL card (if applicable), Bank account",
    applicationProcess: "Through NABARD or implementing NGOs.",
    link: "https://www.nabard.org/content1.aspx?id=23&catid=23&mid=",
    eligibilityCheck: (data) => data.ruralArea || data.belongsToSCST
  },
  {
    id: 36,
    name: "Women Entrepreneurship Platform (WEP)",
    description: "NITI Aayog initiative to foster women entrepreneurship.",
    benefits: "Incubation, funding access, mentorship, and compliance support.",
    eligibility: "Women entrepreneurs and aspiring entrepreneurs.",
    documents: "Identity proof, Business registration (if applicable)",
    applicationProcess: "Register online through the WEP portal.",
    link: "https://wep.gov.in/",
    eligibilityCheck: (data) => data.entrepreneur || data.aspiringEntrepreneur
  },
  {
    id: 37,
    name: "Mahila Udyam Nidhi Scheme",
    description: "Promotes women entrepreneurship by providing equity to women entrepreneurs.",
    benefits: "Soft loans up to 25% of the project cost up to ₹10 lakhs.",
    eligibility: "Women entrepreneurs setting up new projects in tiny/small sectors.",
    documents: "Identity proof, Project report, Estimate of investment",
    applicationProcess: "Through Small Industries Development Bank of India (SIDBI).",
    link: "https://www.sidbi.in/en",
    eligibilityCheck: (data) => data.entrepreneur && data.smallBusiness
  },
  {
    id: 38,
    name: "Stree Shakthi Package for Women Entrepreneurs",
    description: "State Bank of India's scheme to support entrepreneurship among women.",
    benefits: "Concession in interest rates and collateral-free loans up to ₹5 lakhs.",
    eligibility: "Women-led small businesses with majority ownership.",
    documents: "Identity proof, Business plan, Bank account",
    applicationProcess: "Apply through SBI branches.",
    link: "https://www.sbi.co.in/web/business/sme/sme-loans/women-entrepreneurs",
    eligibilityCheck: (data) => data.entrepreneur && data.womenLedBusiness
  },
  {
    id: 39,
    name: "TREAD (Trade Related Entrepreneurship Assistance and Development)",
    description: "Empowers women through trade-related training and development activities.",
    benefits: "Grants up to 30% of loan amount and training assistance.",
    eligibility: "NGOs working with women groups and individual women entrepreneurs.",
    documents: "NGO registration, Project proposal, Women beneficiary details",
    applicationProcess: "Through the Office of the Development Commissioner (MSME).",
    link: "https://msme.gov.in/schemes/women-entrepreneurship",
    eligibilityCheck: (data) => data.entrepreneur || data.ngoWorkingWithWomen
  },
  {
    id: 40,
    name: "Shringaar and Ananya Schemes",
    description: "SIDBI schemes specifically for women entrepreneurs.",
    benefits: "Financial assistance with relaxed terms and conditions.",
    eligibility: "Women entrepreneurs in beauty parlors, boutiques, and other similar ventures.",
    documents: "Identity proof, Business plan, Collateral documents",
    applicationProcess: "Apply through SIDBI branches.",
    link: "https://www.sidbi.in/en/products",
    eligibilityCheck: (data) => data.entrepreneur && (data.beautySector || data.fashionSector)
  }
];

export const allSchemes = [...schemes, ...additionalSchemes];