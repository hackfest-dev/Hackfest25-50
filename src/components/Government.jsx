'use client';

import React, { useState } from 'react';
import { ChevronRight, CheckCircle2, AlertCircle, Search, User, Briefcase, Home, Heart } from 'lucide-react';
import { useTranslation } from 'react-i18next';
import Navbar from './Navbar';
import LanguageSwitcher from './LanguageSwitcher';

const Government = () => {
  const { t } = useTranslation();
  const [searchTerm, setSearchTerm] = useState('');
  const [selectedCategory, setSelectedCategory] = useState('all');
  const [eligibilityAnswers, setEligibilityAnswers] = useState({});

  const categories = [
    { id: 'all', label: 'All Schemes', icon: Home },
    { id: 'financial', label: 'Financial Support', icon: Briefcase },
    { id: 'education', label: 'Education', icon: User },
    { id: 'health', label: 'Health & Wellness', icon: Heart },
  ];

  const schemes = [
    {
      id: 'sukanya-samriddhi',
      title: 'Sukanya Samriddhi Yojana (SSY)',
      description: 'Part of Beti Bachao, Beti Padhao mission for girl child education and welfare',
      category: 'financial',
      eligibility: [
        'Parents of newborn girl child',
        'Scheme duration of 15 years (extendable by 5 years)',
        'Maximum annual investment - Rs. 1.50 lakhs',
        'Indian resident'
      ],
      benefits: [
        'Tax-Free Interest - 8.2% p.a.',
        'Tax deduction under 80C',
        'Funds usable for higher education and marriage',
        'Secure investment for girl child'
      ],
      defaultLink: '@https://shebox.wcd.gov.in/'
    },
    {
      id: 'lakhpati-didi',
      title: 'Lakhpati Didi Scheme',
      description: 'Empowerment scheme for women in Self-Help Groups',
      category: 'financial',
      eligibility: [
        'Women working in Self-Help Groups (SHGs)',
        'Active SHG member',
        'Willing to participate in economic activities'
      ],
      benefits: [
        'Support to build capital over Rs. 1 lakh',
        'Training and skill development',
        'Access to market linkages',
        'Financial inclusion support'
      ],
      defaultLink: '@https://shebox.wcd.gov.in/'
    },
    {
      id: 'drone-didi',
      title: 'Drone Didi Scheme',
      description: 'Training program for women to become drone pilots',
      category: 'education',
      eligibility: [
        'Women from Self Help Groups (SHGs)',
        'Basic education qualification',
        'Age between 18-45 years'
      ],
      benefits: [
        'Free drone pilot training',
        'Employment opportunities',
        'Skill development',
        'Economic independence'
      ],
      defaultLink: '@https://shebox.wcd.gov.in/'
    },
    {
      id: 'mudra-yojana',
      title: 'Pradhan Mantri Mudra Yojana',
      description: 'Collateral-free loans for small business owners',
      category: 'financial',
      eligibility: [
        'Women entrepreneurs',
        'Small business owners',
        'No collateral required',
        'Valid business proposal'
      ],
      benefits: [
        'Loans up to Rs. 20 lakhs',
        'No collateral required',
        'Lower interest rates',
        'Easy documentation'
      ],
      defaultLink: '@https://shebox.wcd.gov.in/'
    },
    {
      id: 'tread-scheme',
      title: 'TREAD (Trade Related Entrepreneurship Development)',
      description: 'Support scheme for women entrepreneurs',
      category: 'financial',
      eligibility: [
        'Women entrepreneurs',
        'Valid business plan',
        'Association with eligible NGO',
        'Indian resident'
      ],
      benefits: [
        '30% government contribution up to Rs. 30 lakhs',
        'NGO support for project handling',
        'Training and counseling',
        'Market linkages'
      ],
      defaultLink: '@https://shebox.wcd.gov.in/'
    },
    {
      id: 'ujjwala',
      title: 'Pradhan Mantri Ujjwala Yojana',
      description: 'Free LPG connections for BPL households',
      category: 'health',
      eligibility: [
        'Women from BPL households',
        'No existing LPG connection',
        'Age 18 years and above'
      ],
      benefits: [
        'Free LPG connection',
        'First refill and stove free',
        'EMI facility for gas stove',
        'Clean cooking fuel access'
      ],
      defaultLink: '@https://shebox.wcd.gov.in/'
    },
    {
      id: 'standup-india',
      title: 'Standup India Mission',
      description: 'Loan facility for SC/ST and women entrepreneurs',
      category: 'financial',
      eligibility: [
        'Women entrepreneurs',
        'SC/ST entrepreneurs',
        'Valid business proposal',
        'Above 18 years of age'
      ],
      benefits: [
        'Loans between Rs. 10 lakhs to Rs. 1 crore',
        'Covers 75% of project cost',
        'Composite loan facility',
        'Business development support'
      ],
      defaultLink: '@https://shebox.wcd.gov.in/'
    },
    {
      id: 'mahila-samman',
      title: 'Mahila Samman Savings Certificate',
      description: '2-year special FD scheme for women',
      category: 'financial',
      eligibility: [
        'Women of any age',
        'Indian resident',
        'Valid identity proof'
      ],
      benefits: [
        '7.5% interest rate p.a.',
        'Deposit up to Rs. 2 lakhs',
        'Secure investment option',
        'Available at post offices'
      ],
      defaultLink: '@https://shebox.wcd.gov.in/'
    },
    {
      id: 'mahila-shakti',
      title: 'Mahila Shakti Kendras',
      description: 'One-stop convergence support for women',
      category: 'education',
      eligibility: [
        'All women',
        'Need for skill development',
        'Interest in digital literacy',
        'Seeking employment support'
      ],
      benefits: [
        'Skill development training',
        'Digital literacy programs',
        'Employment assistance',
        'Support services for women'
      ],
      defaultLink: '@https://shebox.wcd.gov.in/'
    },
    {
      id: 'mahila-ehaat',
      title: 'Mahila E-Haat Scheme',
      description: 'Online marketplace platform for women',
      category: 'financial',
      eligibility: [
        'Women entrepreneurs',
        'Women with products to sell',
        'Self-help groups',
        'Valid documentation'
      ],
      benefits: [
        'Direct market access',
        'Online product showcase',
        'Marketing support',
        'Make in India promotion'
      ],
      defaultLink: '@https://shebox.wcd.gov.in/'
    },
    {
      id: 'mission-indradhanush',
      title: 'Mission Indradhanush',
      description: 'Comprehensive immunization program',
      category: 'health',
      eligibility: [
        'Pregnant women',
        'Children up to 2 years',
        'Indian residents',
        'Unvaccinated or partially vaccinated'
      ],
      benefits: [
        'Free immunization services',
        'Complete vaccination coverage',
        'Health monitoring',
        'Medical support'
      ],
      defaultLink: '@https://shebox.wcd.gov.in/'
    },
    {
      id: 'pm-matru-vandana',
      title: 'Pradhan Mantri Matru Vandana Yojana (PMMVY)',
      description: 'Maternity benefit program providing financial support to pregnant women',
      category: 'health',
      eligibility: [
        'Pregnant and lactating women for first live birth',
        'Age above 19 years',
        'Registered at AWC/Health facility',
        'Possess maternal and child protection card'
      ],
      benefits: [
        'Cash benefit of ₹5,000 in three installments',
        'Direct bank transfer to beneficiary account',
        'Additional incentive under JSY for institutional delivery',
        'Regular health checkups and vaccination'
      ],
      defaultLink: '@https://shebox.wcd.gov.in/'
    },
    {
      id: 'working-women-hostel',
      title: 'Working Women Hostel Scheme',
      description: 'Safe and affordable accommodation for working women',
      category: 'financial',
      eligibility: [
        'Working women with income up to ₹50,000 per month',
        'Women undergoing training for employment',
        'Female students (some seats reserved)',
        'Emergency requirement for accommodation'
      ],
      benefits: [
        'Safe and affordable accommodation',
        'Day care facility for children',
        'Subsidized meals facility',
        'Security and essential amenities'
      ],
      defaultLink: '@https://shebox.wcd.gov.in/'
    },
    {
      id: 'swadhar-greh',
      title: 'Swadhar Greh Scheme',
      description: 'Support system for women in difficult circumstances',
      category: 'health',
      eligibility: [
        'Women victims of difficult circumstances',
        'Women survivors of natural disasters',
        'Women prisoners released from jail',
        'Trafficked women/girls rescued'
      ],
      benefits: [
        'Temporary residential support',
        'Medical care and legal aid',
        'Vocational and skill development training',
        'Rehabilitation through employment'
      ],
      defaultLink: '@https://shebox.wcd.gov.in/'
    },
    {
      id: 'nari-shakti-puraskar',
      title: 'Nari Shakti Puraskar',
      description: 'Highest civilian honor for women in India',
      category: 'education',
      eligibility: [
        'Women working for women empowerment',
        'Significant contribution in any field',
        'Groundbreaking achievements',
        'Innovation in women-centric initiatives'
      ],
      benefits: [
        'Cash prize of ₹2 lakh per awardee',
        'National recognition and certificate',
        'Platform for showcasing achievements',
        'Networking opportunities'
      ],
      defaultLink: '@https://shebox.wcd.gov.in/'
    },
    {
      id: 'poshan-abhiyaan',
      title: 'POSHAN Abhiyaan',
      description: "Prime Minister's Overarching Scheme for Holistic Nutrition",
      category: 'health',
      eligibility: [
        'Pregnant women and lactating mothers',
        'Children under 6 years',
        'Adolescent girls',
        'All women registered at Anganwadi centers'
      ],
      benefits: [
        'Nutritional support and counseling',
        'Regular health check-ups',
        'Growth monitoring of children',
        'Behavioral change programs'
      ],
      defaultLink: '@https://shebox.wcd.gov.in/'
    },
    {
      id: 'women-helpline',
      title: 'Universalization of Women Helpline',
      description: '24x7 emergency response system for women',
      category: 'health',
      eligibility: [
        'Any woman in distress',
        'Victims of violence',
        'Emergency assistance seekers',
        'Need for information about women-related programs'
      ],
      benefits: [
        'Toll-free 24x7 helpline (181)',
        'Immediate emergency response',
        'Integration with police, hospital',
        'Referral to support services'
      ],
      defaultLink: '@https://shebox.wcd.gov.in/'
    },
    {
      id: 'mahila-police-volunteer',
      title: 'Mahila Police Volunteer Initiative',
      description: 'Bridge between police and community for women safety',
      category: 'education',
      eligibility: [
        'Women aged 21-50 years',
        'Minimum 12th pass',
        'Resident of same area',
        'No criminal background'
      ],
      benefits: [
        'Monthly honorarium',
        'Training in legal awareness',
        'Direct link with police authorities',
        'Community leadership role'
      ],
      defaultLink: '@https://shebox.wcd.gov.in/'
    },
    {
      id: 'mahila-shakti-kendra',
      title: 'Mahila Shakti Kendra Scheme',
      description: 'One-stop convergence support services for women',
      category: 'education',
      eligibility: [
        'Rural and urban women',
        'Women seeking skill development',
        'Women needing support services',
        'Community engagement interest'
      ],
      benefits: [
        'Capacity building and training',
        'Employment linkages',
        'Digital literacy support',
        'Access to government schemes'
      ],
      defaultLink: '@https://shebox.wcd.gov.in/'
    },
    {
      id: 'nirbhaya-fund',
      title: 'Nirbhaya Fund Schemes',
      description: 'Integrated support system for women safety',
      category: 'health',
      eligibility: [
        'Women affected by violence',
        'Projects focusing on women safety',
        'Initiatives for women empowerment',
        'Emergency response needs'
      ],
      benefits: [
        'One-stop crisis centers',
        'Emergency response support',
        'Safe city initiatives',
        'Women safety on public transport'
      ],
      defaultLink: '@https://shebox.wcd.gov.in/'
    },
    {
      id: 'kiran-helpline',
      title: 'KIRAN Mental Health Helpline',
      description: '24/7 toll-free mental health rehabilitation helpline',
      category: 'health',
      eligibility: [
        'Women facing mental health issues',
        'Early intervention needs',
        'Psychological support seekers',
        'Crisis intervention requirements'
      ],
      benefits: [
        'Free psychological support',
        'Early screening services',
        'Referral to mental health professionals',
        'Crisis management support'
      ],
      defaultLink: '@https://shebox.wcd.gov.in/'
    }
  ];

  const handleEligibilityAnswer = (schemeId, question, answer) => {
    setEligibilityAnswers(prev => ({
      ...prev,
      [schemeId]: {
        ...prev[schemeId],
        [question]: answer
      }
    }));
  };

  const checkEligibility = (scheme) => {
    const answers = eligibilityAnswers[scheme.id] || {};
    const totalQuestions = scheme.eligibility?.length || 0;
    const answeredYes = Object.values(answers).filter(answer => answer === 'yes').length;
    return totalQuestions > 0 ? (answeredYes / totalQuestions) * 100 : 0;
  };

  const filteredSchemes = schemes.filter(scheme => {
    const matchesSearch = scheme.title.toLowerCase().includes(searchTerm.toLowerCase()) ||
                         scheme.description.toLowerCase().includes(searchTerm.toLowerCase());
    const matchesCategory = selectedCategory === 'all' || scheme.category === selectedCategory;
    return matchesSearch && matchesCategory;
  });

  return (
    <div className="min-h-screen bg-gray-100">
      <Navbar />
      <div className="container mx-auto px-4 py-8">
        <div className="mb-8">
          <h1 className="text-3xl font-bold text-gray-800 mb-4">Government Schemes for Women</h1>
          <div className="flex flex-wrap gap-4 mb-6">
            {categories.map(category => (
              <button
                key={category.id}
                onClick={() => setSelectedCategory(category.id)}
                className={`flex items-center px-4 py-2 rounded-lg ${
                  selectedCategory === category.id
                    ? 'bg-blue-600 text-white'
                    : 'bg-white text-gray-600 hover:bg-gray-50'
                }`}
              >
                <category.icon className="w-5 h-5 mr-2" />
                {category.label}
              </button>
            ))}
          </div>
          <div className="relative">
            <input
              type="text"
              placeholder="Search schemes..."
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
              className="w-full px-4 py-2 rounded-lg border border-gray-300 focus:outline-none focus:ring-2 focus:ring-blue-500"
            />
            <Search className="absolute right-3 top-2.5 text-gray-400 w-5 h-5" />
          </div>
        </div>

        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
          {filteredSchemes.map((scheme) => (
            <div key={scheme.id} className="bg-white rounded-lg shadow-md p-6">
              <h3 className="text-xl font-semibold mb-2">{scheme.title}</h3>
              <p className="text-gray-600 mb-4">{scheme.description}</p>
              
              <div className="mb-4">
                <h4 className="font-semibold mb-2">Check Your Eligibility:</h4>
                {scheme.eligibility.map((criteria, index) => (
                  <div key={index} className="mb-2">
                    <p className="text-gray-700 mb-1">{criteria}</p>
                    <div className="flex gap-2">
                      <button
                        onClick={() => handleEligibilityAnswer(scheme.id, index, 'yes')}
                        className={`px-3 py-1 rounded ${
                          eligibilityAnswers[scheme.id]?.[index] === 'yes'
                            ? 'bg-green-500 text-white'
                            : 'bg-gray-100 text-gray-600'
                        }`}
                      >
                        Yes
                      </button>
                      <button
                        onClick={() => handleEligibilityAnswer(scheme.id, index, 'no')}
                        className={`px-3 py-1 rounded ${
                          eligibilityAnswers[scheme.id]?.[index] === 'no'
                            ? 'bg-red-500 text-white'
                            : 'bg-gray-100 text-gray-600'
                        }`}
                      >
                        No
                      </button>
                    </div>
                  </div>
                ))}
                
                {eligibilityAnswers[scheme.id] && (
                  <div className="mt-4">
                    <div className="flex items-center gap-2">
                      {checkEligibility(scheme) === 100 ? (
                        <>
                          <CheckCircle2 className="text-green-500 w-5 h-5" />
                          <span className="text-green-500">You are eligible!</span>
                        </>
                      ) : (
                        <>
                          <AlertCircle className="text-red-500 w-5 h-5" />
                          <span className="text-red-500">You may not be eligible</span>
                        </>
                      )}
                    </div>
                  </div>
                )}
              </div>

              <div className="mb-4">
                <h4 className="font-semibold mb-2">Benefits:</h4>
                <ul className="list-disc list-inside">
                  {scheme.benefits.map((benefit, index) => (
                    <li key={index} className="text-gray-600">{benefit}</li>
                  ))}
                </ul>
              </div>
              
              <a
                href={scheme.defaultLink}
                target="_blank"
                rel="noopener noreferrer"
                className="inline-flex items-center text-blue-600 hover:text-blue-800"
              >
                Learn More <ChevronRight className="w-4 h-4 ml-1" />
              </a>
            </div>
          ))}
        </div>
      </div>
    </div>
  );
};

export default Government;