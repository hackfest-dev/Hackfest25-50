'use client';

import React, { useState, useRef, useEffect } from 'react';
import { toast } from 'react-toastify';
import Navbar from './Navbar';

const LegalChatbot = () => {
  const [messages, setMessages] = useState([]);
  const [inputMessage, setInputMessage] = useState('');
  const [selectedLanguage, setSelectedLanguage] = useState('en'); // Default language is English
  const [isLoading, setIsLoading] = useState(false);
  const chatContainerRef = useRef(null);
  const messagesEndRef = useRef(null);

  const GROQ_API_KEY = import.meta.env.VITE_GROQ_API_KEY;
  const GROQ_API_URL = 'https://api.groq.com/openai/v1/chat/completions';
  const GOOGLE_TRANSLATE_API_KEY = 'AIzaSyAaB-rfgLYZ_GU1mUruQS7dhc7_cife4s0';

  const languages = [
    { code: 'en', name: 'English' },
    { code: 'hi', name: 'हिंदी' },
    { code: 'kn', name: 'ಕನ್ನಡ' }
  ];

  const scrollToBottom = () => {
    if (chatContainerRef.current) {
      chatContainerRef.current.scrollTop = chatContainerRef.current.scrollHeight;
    }
    messagesEndRef.current?.scrollIntoView({ behavior: "smooth" });
  };

  useEffect(() => {
    scrollToBottom();
  }, [messages]);

  // Function to translate text using Google Translate API
  const translateText = async (text, targetLang) => {
    try {
      const response = await fetch(
        `https://translation.googleapis.com/language/translate/v2?key=${GOOGLE_TRANSLATE_API_KEY}`,
        {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
          },
          body: JSON.stringify({
            q: text,
            target: targetLang,
          }),
        }
      );

      const data = await response.json();
      return data.data.translations[0].translatedText;
    } catch (error) {
      console.error('Translation error:', error);
      return text; // Return original text if translation fails
    }
  };

  // Function to handle language change
  const handleLanguageChange = async (langCode) => {
    setSelectedLanguage(langCode);
    
    // Translate all existing messages to the new language
    const translatedMessages = await Promise.all(
      messages.map(async (msg) => ({
        ...msg,
        text: await translateText(msg.text, langCode),
      }))
    );
    
    setMessages(translatedMessages);
  };

  const systemPrompt = `You are a legal advisor specializing in Indian women's rights and laws. Your role is to provide accurate, empathetic, and actionable legal guidance based on Indian laws. Always maintain a professional and supportive tone.

Key areas of expertise:
1. Domestic Violence
2. Sexual Harassment
3. Property Rights
4. Divorce Laws
5. Workplace Rights
6. Cybercrime
7. Maternity Benefits

For each response, follow this format:
APPLICABLE LAWS AND SECTIONS:
[List relevant laws with specific sections]

YOUR LEGAL RIGHTS:
[List specific rights]

RECOMMENDED LEGAL ACTIONS:
[Step-by-step actions]

AUTHORITIES TO APPROACH:
[List with contact details]

IMMEDIATE STEPS:
[Emergency measures]

ADDITIONAL RESOURCES:
[Helplines and support organizations]

Always include emergency contact numbers:
- Women's Helpline: 181
- Police: 100
- NCW: 1091
- Legal Services: 1516`;

  const processUserStory = async (userStory) => {
    try {
      if (!GROQ_API_KEY || GROQ_API_KEY === 'your-groq-api-key-here') {
        throw new Error('Please configure your Groq API key in the .env file');
      }

      const response = await fetch(GROQ_API_URL, {
        method: 'POST',
        headers: {
          'Authorization': `Bearer ${GROQ_API_KEY}`,
          'Content-Type': 'application/json',
          'Accept': 'application/json'
        },
        body: JSON.stringify({
          model: 'llama-3.3-70b-versatile',
          messages: [
            {
              role: "system",
              content: systemPrompt
            },
            {
              role: "user",
              content: userStory
            }
          ],
          temperature: 0.7,
          max_tokens: 4096,
          top_p: 1,
          stream: false
        })
      });

      if (!response.ok) {
        const errorData = await response.json();
        console.error('Groq API Error:', errorData);
        throw new Error(errorData.error?.message || 'Failed to get response from AI service');
      }

      const data = await response.json();
      if (!data.choices?.[0]?.message?.content) {
        throw new Error('Invalid response format from API');
      }
      return data.choices[0].message.content;
    } catch (error) {
      console.error('Error in processUserStory:', error);
      return getFallbackResponse(userStory.toLowerCase());
    }
  };

  const getFallbackResponse = (query) => {
    if (query.includes('domestic') || query.includes('violence') || query.includes('abuse')) {
      return `APPLICABLE LAWS AND SECTIONS:
- Protection of Women from Domestic Violence Act, 2005
- Section 498A of Indian Penal Code (Cruelty by Husband or his Relatives)
- Section 304B of IPC (Dowry Death)

YOUR LEGAL RIGHTS:
1. Right to reside in the shared household
2. Right to protection orders
3. Right to monetary relief
4. Right to custody orders for children
5. Right to compensation

RECOMMENDED LEGAL ACTIONS:
1. File a Domestic Incident Report (DIR) with Protection Officer
2. Apply for protection order from Magistrate
3. File FIR at local police station
4. Seek maintenance under Section 125 CrPC

AUTHORITIES TO APPROACH:
1. Women's Helpline: 181
2. Police: 100
3. Protection Officer
4. State Women Commission
5. National Commission for Women: 011-26942369

IMMEDIATE STEPS:
1. Ensure your safety first
2. Document incidents and injuries
3. Keep important documents safe
4. Contact emergency helpline
5. Reach out to trusted family/friends

ADDITIONAL RESOURCES:
- National Legal Services Authority: 1516
- Nearest Police Station
- Local Women's Rights NGOs
- Domestic Violence Shelters`;
    }

    if (query.includes('harassment') || query.includes('workplace')) {
      return `APPLICABLE LAWS AND SECTIONS:
- Sexual Harassment of Women at Workplace Act, 2013
- Section 354A IPC (Sexual Harassment)
- Section 509 IPC (Word, Gesture or Act Intended to Insult Modesty)

YOUR LEGAL RIGHTS:
1. Right to safe working environment
2. Right to file complaint with ICC/LCC
3. Right to request transfer
4. Protection from retaliation
5. Right to confidentiality

RECOMMENDED LEGAL ACTIONS:
1. File written complaint to Internal Complaints Committee
2. Document all incidents with date and time
3. Gather evidence and witness statements
4. File police complaint if criminal in nature

AUTHORITIES TO APPROACH:
1. Internal Complaints Committee
2. Local Complaints Committee
3. Women's Helpline: 181
4. Police: 100
5. State Women Commission

IMMEDIATE STEPS:
1. Report to ICC/supervisor
2. Document everything in writing
3. Save all communication
4. Identify witnesses
5. Seek counseling support

ADDITIONAL RESOURCES:
- She-Box Portal
- National Commission for Women
- Workplace Rights NGOs`;
    }

    // Default response for other queries
    return `I understand you need legal assistance. Here are the key emergency contacts and resources:

🚨 EMERGENCY CONTACTS:
• Women's Helpline: 181
• Police: 100
• NCW Helpline: 1091
• Legal Services: 1516

IMMEDIATE STEPS:
1. Document your situation in detail
2. Contact the emergency helpline
3. Reach out to the authorities
4. Seek legal counsel

ONLINE RESOURCES:
• National Commission for Women: http://www.ncw.nic.in
• Ministry of Women & Child Development: https://wcd.nic.in
• Legal Services Authority: https://nalsa.gov.in

Please provide more specific details about your situation (e.g., domestic violence, workplace harassment, property rights, etc.) so I can give you more targeted guidance.`;
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!inputMessage.trim()) return;

    const userMessage = {
      text: inputMessage,
      sender: 'user',
      timestamp: new Date().toLocaleTimeString()
    };

    setMessages(prev => [...prev, userMessage]);
    setInputMessage('');
    setIsLoading(true);

    try {
      const botResponse = await processUserStory(inputMessage);
      
      const botMessage = {
        text: botResponse,
        sender: 'bot',
        timestamp: new Date().toLocaleTimeString()
      };

      setMessages(prev => [...prev, botMessage]);
    } catch (error) {
      console.error('Error in chat handling:', error);
      const errorMessage = {
        text: getFallbackResponse(inputMessage.toLowerCase()),
        sender: 'bot',
        timestamp: new Date().toLocaleTimeString()
      };
      setMessages(prev => [...prev, errorMessage]);
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <div className="min-h-screen bg-gray-100">
      <Navbar />
      <div className="container mx-auto px-4 py-8">
        <div className="flex flex-col h-[80vh] max-w-4xl mx-auto bg-white rounded-lg shadow-lg overflow-hidden">
          <div className="bg-purple-600 p-4 text-white">
            <h2 className="text-xl font-semibold">Legal Rights Assistant</h2>
            <p className="text-sm opacity-80">Share your story, and I'll help you understand your legal rights</p>
          </div>

          <div className="mb-4 flex justify-end">
            <select
              value={selectedLanguage}
              onChange={(e) => handleLanguageChange(e.target.value)}
              className="px-4 py-2 rounded-lg border border-gray-300 focus:outline-none focus:ring-2 focus:ring-blue-500"
            >
              {languages.map((lang) => (
                <option key={lang.code} value={lang.code}>
                  {lang.name}
                </option>
              ))}
            </select>
          </div>

          <div 
            ref={chatContainerRef}
            className="flex-1 overflow-y-auto p-4 space-y-4"
          >
            {messages.length === 0 && (
              <div className="text-center text-gray-500 mt-4">
                <p>Welcome! Please share your situation, and I'll provide relevant legal information and guidance based on Indian laws.</p>
                <p className="mt-2 text-sm">Your privacy is important to us. All conversations are confidential.</p>
                <div className="mt-4 text-sm bg-purple-50 p-4 rounded-lg">
                  <p className="font-semibold">I can help you with:</p>
                  <ul className="list-disc list-inside mt-2">
                    <li>Domestic Violence</li>
                    <li>Sexual Harassment</li>
                    <li>Property Rights</li>
                    <li>Divorce Laws</li>
                    <li>Workplace Rights</li>
                    <li>Cybercrime</li>
                    <li>Maternity Benefits</li>
                  </ul>
                </div>
              </div>
            )}
            {messages.map((message, index) => (
              <div
                key={index}
                className={`flex ${message.sender === 'user' ? 'justify-end' : 'justify-start'} mb-4`}
              >
                <div
                  className={`max-w-[80%] rounded-lg p-4 ${
                    message.sender === 'user'
                      ? 'bg-purple-600 text-white'
                      : 'bg-white shadow-lg text-gray-800 border border-gray-200'
                  }`}
                >
                  {message.sender === 'bot' && (
                    <div className="flex items-center mb-2">
                      <span className="text-purple-600 mr-2">⚖️</span>
                      <span className="font-semibold text-purple-600">Legal Assistant</span>
                    </div>
                  )}
                  {message.sender === 'user' ? (
                    <p className="text-sm whitespace-pre-wrap">{message.text}</p>
                  ) : (
                    <div className="legal-response">
                      {message.text.split('\n\n').map((section, sectionIndex) => {
                        if (section.includes(':')) {
                          const [title, ...content] = section.split('\n');
                          return (
                            <div key={sectionIndex} className="mb-4">
                              <h3 className="font-bold text-purple-800 mb-2">
                                {title.replace(':', '')}
                              </h3>
                              <div className="pl-4">
                                {content.map((line, lineIndex) => (
                                  <p 
                                    key={lineIndex} 
                                    className={`mb-1 ${
                                      line.startsWith('•') || line.startsWith('-') || /^\d+\./.test(line)
                                        ? 'flex'
                                        : ''
                                    }`}
                                  >
                                    {(line.startsWith('•') || line.startsWith('-')) && (
                                      <span className="mr-2 text-purple-600">•</span>
                                    )}
                                    {/^\d+\./.test(line) && (
                                      <span className="mr-2 text-purple-600 font-semibold">
                                        {line.split('.')[0]}.
                                      </span>
                                    )}
                                    <span>
                                      {(line.startsWith('•') || line.startsWith('-'))
                                        ? line.substring(1).trim()
                                        : /^\d+\./.test(line)
                                        ? line.split('.').slice(1).join('.').trim()
                                        : line.trim()}
                                    </span>
                                  </p>
                                ))}
                              </div>
                            </div>
                          );
                        }
                        return (
                          <p key={sectionIndex} className="mb-4 text-gray-700">
                            {section}
                          </p>
                        );
                      })}
                    </div>
                  )}
                  <p className="text-xs mt-2 opacity-70">{message.timestamp}</p>
                </div>
              </div>
            ))}
            {isLoading && (
              <div className="flex justify-start mb-4">
                <div className="bg-white shadow-lg rounded-lg p-4 flex items-center space-x-2">
                  <span className="text-purple-600">⚖️</span>
                  <div className="flex space-x-1">
                    <div className="w-2 h-2 bg-purple-600 rounded-full animate-bounce"></div>
                    <div className="w-2 h-2 bg-purple-600 rounded-full animate-bounce" style={{ animationDelay: '0.2s' }}></div>
                    <div className="w-2 h-2 bg-purple-600 rounded-full animate-bounce" style={{ animationDelay: '0.4s' }}></div>
                  </div>
                </div>
              </div>
            )}
          </div>

          <form onSubmit={handleSubmit} className="p-4 border-t">
            <div className="flex space-x-2">
              <textarea
                value={inputMessage}
                onChange={(e) => setInputMessage(e.target.value)}
                placeholder="Share your story here... Press Enter to send, Shift + Enter for new line"
                className="flex-1 p-2 border rounded-lg focus:outline-none focus:border-purple-600 resize-none"
                rows="3"
                onKeyDown={(e) => {
                  if (e.key === 'Enter' && !e.shiftKey) {
                    e.preventDefault();
                    handleSubmit(e);
                  }
                }}
              />
              <button
                type="submit"
                disabled={isLoading}
                className="bg-purple-600 text-white px-4 py-2 rounded-lg hover:bg-purple-700 transition-colors disabled:opacity-50 h-fit"
              >
                Send
              </button>
            </div>
          </form>
        </div>
      </div>
    </div>
  );
};

export default LegalChatbot;