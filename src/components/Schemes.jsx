import React, { useState } from 'react';
import { ChevronDown, ChevronUp, Search, ExternalLink, HelpCircle, CheckCircle2, AlertCircle } from 'lucide-react';
import Navbar from './Navbar';

const Schemes = () => {
  const [currentStep, setCurrentStep] = useState(1);
  const [formData, setFormData] = useState({
    // Personal Information
    age: '',
    gender: '',
    category: '', // caste
    disability: '',
    
    // Location & Family
    state: '',
    residenceType: '', // home resident type
    familyIncome: '', // monthly income
    isBPL: false,
    
    // Education & Status
    educationLevel: '',
    isPregnant: false,
  });

  const [showResults, setShowResults] = useState(false);
  const [expandedScheme, setExpandedScheme] = useState(null);
  const [errors, setErrors] = useState({});

  const schemes = [
    {
      id: 'sukanyaSamriddhi',
      title: 'Sukanya Samriddhi Yojana',
      description: 'A small deposit scheme for girl child to ensure a secure future',
      eligibility: [
        'Girl child aged below 10 years',
        'Only for Indian citizens',
        'Maximum two girl children in a family',
        'Valid bank account and Aadhaar card'
      ],
      benefits: [
        'Tax benefits under Section 80C of Income Tax Act',
        'Higher interest rate (currently 8.0% per annum)',
        'Partial withdrawal allowed for higher education after girl turns 18',
        'Maturity period of 21 years from date of opening'
      ],
      documents: [
        'Birth certificate of girl child',
        'Identity proof of parents/guardian',
        'Address proof',
        'Passport size photographs'
      ],
      link: 'https://www.india.gov.in/spotlight/sukanya-samriddhi-yojana',
      checkEligibility: (data) => {
        return data.hasChildren && data.age < 45 && data.hasBankAccount;
      }
    },
    {
      id: 'pmMatruVandana',
      title: 'Pradhan Mantri Matru Vandana Yojana (PMMVY)',
      description: 'Maternity benefit program for pregnant women and lactating mothers',
      eligibility: [
        'Pregnant women and lactating mothers',
        'First live birth',
        'Age above 19 years'
      ],
      benefits: [
        'Cash benefit of ₹5,000 in three installments',
        'Additional incentive of ₹1,000 under JSY',
        'Free antenatal check-ups'
      ],
      link: 'https://www.india.gov.in/advance-search/results?title=women&term_node_tid_depth=All&sort_by=changed&sort_order=DESC&Advance+search+Submit+Botton=Search&as_fid=01e34e90ada40469783f62add538e431591ab3ed',
      checkEligibility: (data) => {
        return data.isPregnant && data.age >= 19;
      }
    },
    {
      id: 'workingWomenHostel',
      title: 'Working Women Hostel Scheme',
      description: 'Provides safe and affordable accommodation to working women',
      eligibility: [
        'Working women with income under ₹50,000 per month in metro cities',
        'Women undergoing training for employment',
        'Single working women'
      ],
      benefits: [
        'Safe and affordable accommodation',
        'Day care center for children',
        'Security and medical facilities'
      ],
      link: 'https://www.india.gov.in/advance-search/results?title=women&term_node_tid_depth=All&sort_by=changed&sort_order=DESC&Advance+search+Submit+Botton=Search&as_fid=01e34e90ada40469783f62add538e431591ab3ed',
      checkEligibility: (data) => {
        return data.isWorking && data.gender === 'female' && 
               (data.income === 'below50k' || data.income === 'below25k');
      }
    },
    {
      id: 'mahilaShakti',
      title: 'Mahila Shakti Kendra',
      description: 'Empowers rural women through community participation',
      eligibility: [
        'Rural women aged 18 and above',
        'Women living in backward areas',
        'Priority to women from marginalized communities'
      ],
      benefits: [
        'Skill development training',
        'Financial literacy programs',
        'Support for starting micro-enterprises'
      ],
      link: 'https://www.india.gov.in/advance-search/results?title=women&term_node_tid_depth=All&sort_by=changed&sort_order=DESC&Advance+search+Submit+Botton=Search&as_fid=01e34e90ada40469783f62add538e431591ab3ed',
      checkEligibility: (data) => {
        return data.gender === 'female' && data.age >= 18 && data.location === 'rural';
      }
    },
    {
      id: 'ujjwala',
      title: 'Pradhan Mantri Ujjwala Yojana',
      description: 'Provides LPG connections to women from BPL households',
      eligibility: [
        'Women from BPL households',
        'No existing LPG connection',
        'Age 18 years and above'
      ],
      benefits: [
        'Free LPG connection',
        'First refill and stove free of cost',
        'EMI facility for gas stove'
      ],
      link: 'https://www.india.gov.in/advance-search/results?title=women&term_node_tid_depth=All&sort_by=changed&sort_order=DESC&Advance+search+Submit+Botton=Search&as_fid=01e34e90ada40469783f62add538e431591ab3ed',
      checkEligibility: (data) => {
        return data.gender === 'female' && data.age >= 18 && 
               (data.income === 'below25k' || data.income === 'bpl');
      }
    }
  ];

  const tooltips = {
    age: "Your current age helps us find age-specific schemes",
    gender: "Different schemes are available based on gender",
    category: "Special schemes are available for SC/ST/OBC categories",
    disability: "Additional benefits may be available for persons with disabilities",
    state: "State-specific schemes will be shown based on your location",
    residenceType: "Type of residence (Own/Rented/Other)",
    familyIncome: "Monthly family income helps determine eligibility for various schemes",
    isBPL: "Whether you have a Below Poverty Line card",
    educationLevel: "Your current education level",
    isPregnant: "Special schemes available for expecting mothers"
  };

  const fieldOptions = {
    gender: ['Female', 'Male', 'Other'],
    category: ['General', 'SC', 'ST', 'OBC', 'Other'],
    state: ['Maharashtra', 'Delhi', 'Karnataka', 'Tamil Nadu', 'Other'],
    residenceType: ['Own House', 'Rented', 'Other'],
    educationLevel: ['Primary', 'Secondary', 'Graduate', 'Post Graduate', 'Other'],
    familyIncome: ['Below 10000', '10000-25000', '25000-50000', 'Above 50000']
  };

  const formSteps = [
    {
      title: 'Personal Information',
      fields: ['age', 'gender', 'category', 'disability']
    },
    {
      title: 'Location & Income',
      fields: ['state', 'residenceType', 'familyIncome', 'isBPL']
    },
    {
      title: 'Education & Status',
      fields: ['educationLevel', 'isPregnant']
    }
  ];

  const handleInputChange = (e) => {
    const { name, value, type, checked } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: type === 'checkbox' ? checked : value
    }));
  };

  const validateStep = (step) => {
    const currentFields = formSteps[step - 1].fields;
    const newErrors = {};
    
    currentFields.forEach(field => {
      if (!formData[field] && formData[field] !== false) {
        newErrors[field] = 'This field is required';
      }
    });

    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const handleNextStep = () => {
    if (validateStep(currentStep)) {
      setCurrentStep(prev => Math.min(prev + 1, formSteps.length));
    }
  };

  const handlePrevStep = () => {
    setCurrentStep(prev => Math.max(prev - 1, 1));
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    if (validateStep(currentStep)) {
      setShowResults(true);
    }
  };

  const getEligibleSchemes = () => {
    return schemes.filter(scheme => scheme.checkEligibility(formData));
  };

  const getStepCompletion = (step) => {
    const fields = formSteps[step - 1].fields;
    const filledFields = fields.filter(field => 
      formData[field] !== '' && formData[field] !== undefined && formData[field] !== null
    );
    return Math.round((filledFields.length / fields.length) * 100);
  };

  const renderTooltip = (fieldName) => (
    <div className="absolute z-10 w-64 px-3 py-2 text-sm font-medium text-white transition-opacity duration-300 bg-gray-900 rounded-lg shadow-sm opacity-0 tooltip dark:bg-gray-700">
      {tooltips[fieldName]}
      <div className="tooltip-arrow"></div>
    </div>
  );

  const renderFormField = (fieldName) => {
    const getLabel = (name) => {
      return name.replace(/([A-Z])/g, ' $1').replace(/^./, str => str.toUpperCase());
    };

    const baseInputClasses = "w-full p-3 border rounded-lg transition-all duration-300 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent";
    const errorInputClasses = errors[fieldName] ? "border-red-500 bg-red-50" : "border-gray-300 hover:border-gray-400";
    const successInputClasses = formData[fieldName] && !errors[fieldName] ? "border-green-500 bg-green-50" : "";

    const renderFieldIcon = () => {
      if (errors[fieldName]) {
        return <AlertCircle className="w-5 h-5 text-red-500" />;
      }
      if (formData[fieldName] && !errors[fieldName]) {
        return <CheckCircle2 className="w-5 h-5 text-green-500" />;
      }
      return null;
    };

    switch (fieldName) {
      case 'gender':
      case 'category':
      case 'state':
      case 'residenceType':
      case 'educationLevel':
      case 'familyIncome':
        return (
          <div key={fieldName} className="mb-6 relative group">
            <label className="block text-gray-700 mb-2 flex items-center text-sm font-medium">
              {getLabel(fieldName)}
              <div className="relative ml-2">
                <HelpCircle className="w-4 h-4 text-gray-400 cursor-help" />
                <div className="invisible group-hover:visible absolute left-full ml-2 p-2 bg-gray-800 text-white text-xs rounded shadow-lg w-48 z-10">
                  {tooltips[fieldName]}
                </div>
              </div>
            </label>
            <div className="relative">
              <select
                name={fieldName}
                value={formData[fieldName]}
                onChange={handleInputChange}
                className={`${baseInputClasses} ${errorInputClasses} ${successInputClasses} pr-10`}
              >
                <option value="">Select {getLabel(fieldName)}</option>
                {fieldOptions[fieldName].map(option => (
                  <option key={option} value={option.toLowerCase()}>{option}</option>
                ))}
              </select>
              <div className="absolute right-3 top-1/2 transform -translate-y-1/2">
                {renderFieldIcon()}
              </div>
            </div>
            {errors[fieldName] && (
              <p className="mt-1 text-sm text-red-600 flex items-center">
                <AlertCircle className="w-4 h-4 mr-1" />
                {errors[fieldName]}
              </p>
            )}
          </div>
        );
      
      case 'isPregnant':
      case 'isBPL':
        return (
          <div key={fieldName} className="mb-6 relative group">
            <label className="flex items-center space-x-3 text-gray-700 cursor-pointer p-3 rounded-lg border border-gray-300 hover:bg-gray-50 transition-colors">
              <input
                type="checkbox"
                name={fieldName}
                checked={formData[fieldName]}
                onChange={handleInputChange}
                className="w-5 h-5 rounded text-blue-500 focus:ring-blue-500 transition-colors"
              />
              <span className="text-sm font-medium">{getLabel(fieldName)}</span>
              <div className="relative ml-2">
                <HelpCircle className="w-4 h-4 text-gray-400 cursor-help" />
                <div className="invisible group-hover:visible absolute left-full ml-2 p-2 bg-gray-800 text-white text-xs rounded shadow-lg w-48 z-10">
                  {tooltips[fieldName]}
                </div>
              </div>
            </label>
          </div>
        );

      default:
        return (
          <div key={fieldName} className="mb-6 relative group">
            <label className="block text-gray-700 mb-2 flex items-center text-sm font-medium">
              {getLabel(fieldName)}
              <div className="relative ml-2">
                <HelpCircle className="w-4 h-4 text-gray-400 cursor-help" />
                <div className="invisible group-hover:visible absolute left-full ml-2 p-2 bg-gray-800 text-white text-xs rounded shadow-lg w-48 z-10">
                  {tooltips[fieldName]}
                </div>
              </div>
            </label>
            <div className="relative">
              <input
                type={fieldName.includes('Number') || fieldName === 'age' ? 'number' : 'text'}
                name={fieldName}
                value={formData[fieldName]}
                onChange={handleInputChange}
                className={`${baseInputClasses} ${errorInputClasses} ${successInputClasses} pr-10`}
                placeholder={`Enter ${getLabel(fieldName)}`}
              />
              <div className="absolute right-3 top-1/2 transform -translate-y-1/2">
                {renderFieldIcon()}
              </div>
            </div>
            {errors[fieldName] && (
              <p className="mt-1 text-sm text-red-600 flex items-center">
                <AlertCircle className="w-4 h-4 mr-1" />
                {errors[fieldName]}
              </p>
            )}
          </div>
        );
    }
  };

  return (
    <div className="min-h-screen bg-gray-100">
      <Navbar />
      <div className="p-4">
        <div className="max-w-4xl mx-auto">
          <div className="mb-8 text-center">
            <h1 className="text-4xl font-bold text-gray-800 mb-4">Find Your Eligible Government Schemes</h1>
            <p className="text-xl text-gray-600">Complete your profile to discover schemes tailored for you</p>
          </div>

          {!showResults ? (
            <div className="bg-white rounded-xl shadow-lg p-8">
              {/* Progress Bar */}
              <div className="mb-12">
                <div className="flex justify-between mb-4">
                  {formSteps.map((step, index) => (
                    <div
                      key={index}
                      className={`flex-1 text-center ${
                        index + 1 === currentStep
                          ? 'text-blue-600'
                          : index + 1 < currentStep
                          ? 'text-green-600'
                          : 'text-gray-400'
                      }`}
                    >
                      <div className="relative">
                        <div
                          className={`w-10 h-10 mx-auto rounded-full flex items-center justify-center border-2 transition-all duration-300 ${
                            index + 1 === currentStep
                              ? 'border-blue-600 bg-blue-100 scale-110'
                              : index + 1 < currentStep
                              ? 'border-green-600 bg-green-100'
                              : 'border-gray-300'
                          }`}
                        >
                          {index + 1 < currentStep ? (
                            <CheckCircle2 className="w-6 h-6 text-green-600" />
                          ) : (
                            <span className="text-lg">{index + 1}</span>
                          )}
                        </div>
                        <div className="mt-2 text-sm font-medium">{step.title}</div>
                        <div className="text-xs mt-1">
                          {getStepCompletion(index + 1)}% Complete
                        </div>
                      </div>
                    </div>
                  ))}
                </div>
                <div className="relative pt-1">
                  <div className="flex mb-2 items-center justify-between">
                    <div className="w-full bg-gray-200 rounded-full h-3">
                      <div
                        className="bg-blue-600 h-3 rounded-full transition-all duration-500 ease-in-out"
                        style={{ width: `${((currentStep - 1) / (formSteps.length - 1)) * 100}%` }}
                      ></div>
                    </div>
                  </div>
                </div>
              </div>

              <form onSubmit={handleSubmit} className="space-y-6">
                <div className="bg-gray-50 rounded-lg p-6 border border-gray-200">
                  <h3 className="text-xl font-semibold text-gray-800 mb-4">{formSteps[currentStep - 1].title}</h3>
                  <p className="text-gray-600 mb-6">Please fill in the following details accurately</p>
                  <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                    {formSteps[currentStep - 1].fields.map(field => renderFormField(field))}
                  </div>
                </div>

                <div className="flex justify-between mt-8">
                  {currentStep > 1 && (
                    <button
                      type="button"
                      onClick={handlePrevStep}
                      className="px-6 py-3 text-gray-600 border border-gray-300 rounded-lg hover:bg-gray-50 transition-colors flex items-center"
                    >
                      <ChevronDown className="w-5 h-5 mr-2 rotate-90" />
                      Previous
                    </button>
                  )}
                  {currentStep < formSteps.length ? (
                    <button
                      type="button"
                      onClick={handleNextStep}
                      className="px-6 py-3 bg-blue-600 text-white rounded-lg hover:bg-blue-700 transition-colors flex items-center ml-auto"
                    >
                      Next
                      <ChevronDown className="w-5 h-5 ml-2 -rotate-90" />
                    </button>
                  ) : (
                    <button
                      type="submit"
                      className="px-6 py-3 bg-green-600 text-white rounded-lg hover:bg-green-700 transition-colors flex items-center ml-auto"
                    >
                      Find Eligible Schemes
                      <Search className="w-5 h-5 ml-2" />
                    </button>
                  )}
                </div>
              </form>
            </div>
          ) : (
            <div className="bg-white rounded-lg shadow-md p-6">
              <h2 className="text-2xl font-semibold mb-6">Eligible Schemes</h2>
              <div className="space-y-6">
                {getEligibleSchemes().map((scheme) => (
                  <div
                    key={scheme.id}
                    className="border rounded-lg p-4 hover:shadow-md transition-shadow"
                  >
                    <div
                      className="flex justify-between items-center cursor-pointer"
                      onClick={() => setExpandedScheme(expandedScheme === scheme.id ? null : scheme.id)}
                    >
                      <h3 className="text-xl font-medium text-gray-800">{scheme.title}</h3>
                      {expandedScheme === scheme.id ? (
                        <ChevronUp className="w-5 h-5 text-gray-500" />
                      ) : (
                        <ChevronDown className="w-5 h-5 text-gray-500" />
                      )}
                    </div>
                    
                    <p className="text-gray-600 mt-2">{scheme.description}</p>
                    
                    {expandedScheme === scheme.id && (
                      <div className="mt-4 space-y-4">
                        <div>
                          <h4 className="font-semibold text-gray-700 mb-2">Eligibility Criteria</h4>
                          <ul className="list-disc list-inside text-gray-600 space-y-1">
                            {scheme.eligibility.map((criteria, index) => (
                              <li key={index}>{criteria}</li>
                            ))}
                          </ul>
                        </div>
                        
                        <div>
                          <h4 className="font-semibold text-gray-700 mb-2">Benefits</h4>
                          <ul className="list-disc list-inside text-gray-600 space-y-1">
                            {scheme.benefits.map((benefit, index) => (
                              <li key={index}>{benefit}</li>
                            ))}
                          </ul>
                        </div>

                        {scheme.documents && (
                          <div>
                            <h4 className="font-semibold text-gray-700 mb-2">Required Documents</h4>
                            <ul className="list-disc list-inside text-gray-600 space-y-1">
                              {scheme.documents.map((doc, index) => (
                                <li key={index}>{doc}</li>
                              ))}
                            </ul>
                          </div>
                        )}
                        
                        <a
                          href={scheme.link}
                          target="_blank"
                          rel="noopener noreferrer"
                          className="inline-flex items-center text-blue-600 hover:text-blue-800"
                        >
                          Learn More <ExternalLink className="w-4 h-4 ml-1" />
                        </a>
                      </div>
                    )}
                  </div>
                ))}
              </div>
              
              <button
                onClick={() => setShowResults(false)}
                className="mt-6 px-4 py-2 text-gray-600 border border-gray-300 rounded hover:bg-gray-50"
              >
                Start Over
              </button>
            </div>
          )}
        </div>
      </div>
    </div>
  );
};

export default Schemes; 