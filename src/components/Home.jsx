import { useState, useEffect } from 'react';
import { Search, ChevronRight, Shield, BookOpen, Users, FileText, Info, Home, Map, Bell, ExternalLink, Clock, Globe } from 'lucide-react';
import { useNavigate } from 'react-router-dom';
import { TypeAnimation } from 'react-type-animation';
import Navbar from './Navbar';
import backgroundVideo from '../assets/viedo.mp4';

// Homepage Component
export function WomenAwarenessHomepage() {
  const [activeTab, setActiveTab] = useState('home');
  const [newsArticles, setNewsArticles] = useState([]);
  const [isLoading, setIsLoading] = useState(true);
  const navigate = useNavigate();

  useEffect(() => {
    // Fetch news about women in India
    const fetchNews = async () => {
      try {
        setIsLoading(true);
        const response = await fetch(
          `https://newsapi.org/v2/everything?q=women+india+rights+empowerment&apiKey=1df962aab8d146e4ad296bc028947716&pageSize=10&language=en&sortBy=publishedAt`
        );
        const data = await response.json();
        if (data.status === 'ok') {
          setNewsArticles(data.articles);
        } else {
          console.error('Failed to fetch news:', data.message);
          setNewsArticles(fallbackNews);
        }
      } catch (error) {
        console.error('Error fetching news:', error);
        setNewsArticles(fallbackNews);
      } finally {
        setIsLoading(false);
      }
    };

    fetchNews();
  }, []);

  const handleNavigation = (path) => {
    navigate(path);
  };

  return (
    <div className="flex flex-col min-h-screen bg-white">
      <Navbar />
      
      {/* Main Content */}
      <main className="flex-1 p-4">
        {/* Welcome Section */}
        <div className="relative h-[400px] rounded-xl overflow-hidden mb-6">
          {/* Video Background */}
          <video
            autoPlay
            loop
            muted
            className="absolute inset-0 w-full h-full object-cover"
            style={{ filter: 'brightness(0.4)' }}
          >
            <source src={backgroundVideo} type="video/mp4" />
            Your browser does not support the video tag.
          </video>

          {/* Content Overlay */}
          <div className="relative h-full flex flex-col justify-center items-center text-center z-10 p-8">
            <TypeAnimation
              sequence={[
                'Welcome to SAADHAN',
                1000,
                'Empowering Women',
                1000,
                'Supporting Rights',
                1000,
                'Welcome to SAADHAN',
                1000,
              ]}
              wrapper="h2"
              speed={50}
              className="text-4xl font-bold text-white mb-4"
              repeat={Infinity}
            />
            <p className="text-xl text-white/90 max-w-2xl">
              Your trusted platform for women's empowerment, legal rights, and support services.
            </p>
          </div>
        </div>

        {/* Feature Cards */}
        <div className="mb-10">
          <h3 className="font-medium mb-8 text-gray-700 flex items-center">
            <span className="text-3xl font-bold bg-gradient-to-r from-blue-600 to-purple-600 bg-clip-text text-transparent">Essentials</span>
            <div className="ml-4 h-1 flex-grow bg-gradient-to-r from-blue-600/20 to-purple-600/20 rounded-full"></div>
          </h3>
          
          <div className="grid grid-cols-1 md:grid-cols-3 gap-8">
            {/* Legal Rights Card */}
            <div className="group relative overflow-hidden rounded-2xl transition-all duration-500 hover:shadow-2xl hover:-translate-y-1">
              <div className="absolute inset-0 bg-gradient-to-br from-blue-500 via-blue-600 to-indigo-700"></div>
              <div className="absolute inset-0 bg-grid-white/[0.05]"></div>
              <div className="relative p-8 flex flex-col h-[320px] justify-between">
                <div>
                  <div className="w-14 h-14 rounded-full bg-white/10 backdrop-blur-xl flex items-center justify-center mb-6 group-hover:scale-110 transition-transform duration-300 border border-white/10">
                    <Shield className="w-7 h-7 text-white" />
                  </div>
                  <h3 className="text-2xl font-bold text-white mb-4">Legal Rights</h3>
                  <p className="text-white/90 leading-relaxed">Know your legal rights and protections under Indian law. Get expert guidance and support.</p>
                </div>
                <button 
                  onClick={() => handleNavigation('/legal')}
                  className="mt-6 px-6 py-3 rounded-xl bg-white/10 text-white font-medium hover:bg-white/20 transition-all duration-300 flex items-center group/btn border border-white/10"
                >
                  Learn More
                  <ChevronRight className="w-4 h-4 ml-2 group-hover/btn:translate-x-1 transition-transform" />
                </button>
              </div>
            </div>

            {/* Government Schemes Card */}
            <div className="group relative overflow-hidden rounded-2xl transition-all duration-500 hover:shadow-2xl hover:-translate-y-1">
              <div className="absolute inset-0 bg-gradient-to-br from-purple-500 via-purple-600 to-indigo-700"></div>
              <div className="absolute inset-0 bg-grid-white/[0.05]"></div>
              <div className="relative p-8 flex flex-col h-[320px] justify-between">
                <div>
                  <div className="w-14 h-14 rounded-full bg-white/10 backdrop-blur-xl flex items-center justify-center mb-6 group-hover:scale-110 transition-transform duration-300 border border-white/10">
                    <FileText className="w-7 h-7 text-white" />
                  </div>
                  <h3 className="text-2xl font-bold text-white mb-4">Govt Schemes</h3>
                  <p className="text-white/90 leading-relaxed">Access government schemes and programs designed for women's welfare and empowerment.</p>
                </div>
                <button 
                  onClick={() => handleNavigation('/schemes')}
                  className="mt-6 px-6 py-3 rounded-xl bg-white/10 text-white font-medium hover:bg-white/20 transition-all duration-300 flex items-center group/btn border border-white/10"
                >
                  Explore Schemes
                  <ChevronRight className="w-4 h-4 ml-2 group-hover/btn:translate-x-1 transition-transform" />
                </button>
              </div>
            </div>

            {/* NGO Connect Card */}
            <div className="group relative overflow-hidden rounded-2xl transition-all duration-500 hover:shadow-2xl hover:-translate-y-1">
              <div className="absolute inset-0 bg-gradient-to-br from-pink-500 via-rose-600 to-rose-700"></div>
              <div className="absolute inset-0 bg-grid-white/[0.05]"></div>
              <div className="relative p-8 flex flex-col h-[320px] justify-between">
                <div>
                  <div className="w-14 h-14 rounded-full bg-white/10 backdrop-blur-xl flex items-center justify-center mb-6 group-hover:scale-110 transition-transform duration-300 border border-white/10">
                    <Users className="w-7 h-7 text-white" />
                  </div>
                  <h3 className="text-2xl font-bold text-white mb-4">NGO Connect</h3>
                  <p className="text-white/90 leading-relaxed">Find support from verified NGOs near you. Connect with organizations that can help.</p>
                </div>
                <button 
                  onClick={() => handleNavigation('/ngos')}
                  className="mt-6 px-6 py-3 rounded-xl bg-white/10 text-white font-medium hover:bg-white/20 transition-all duration-300 flex items-center group/btn border border-white/10"
                >
                  Find NGOs
                  <ChevronRight className="w-4 h-4 ml-2 group-hover/btn:translate-x-1 transition-transform" />
                </button>
              </div>
            </div>
          </div>
        </div>

        {/* News Section */}
        <div className="mb-10">
          <h3 className="font-medium mb-6 text-gray-700 flex items-center">
            <span className="text-xl">Latest News</span>
            <div className="ml-2 h-1 flex-grow bg-blue-100 rounded-full"></div>
          </h3>
          
          {isLoading ? (
            <div className="space-y-4">
              {[1, 2, 3, 4].map((index) => (
                <div key={index} className="flex gap-4 bg-white p-3 rounded-lg border border-blue-100 animate-pulse">
                  <div className="w-32 h-24 bg-blue-50 rounded-lg"></div>
                  <div className="flex-1">
                    <div className="h-4 bg-blue-50 rounded w-3/4 mb-2"></div>
                    <div className="h-3 bg-blue-50 rounded w-1/2"></div>
                  </div>
                </div>
              ))}
            </div>
          ) : (
            <div className="space-y-4">
              {newsArticles.slice(0, 6).map((article, index) => (
                <a 
                  key={index}
                  href={article.url}
                  target="_blank"
                  rel="noopener noreferrer"
                  className="flex gap-4 bg-white p-3 rounded-lg border border-blue-100 hover:shadow-md transition-shadow group"
                >
                  <div className="w-32 h-24 rounded-lg overflow-hidden flex-shrink-0">
                    <img 
                      src={article.urlToImage || 'https://images.unsplash.com/photo-1589578228447-e1a4e481c6c8?ixlib=rb-1.2.1&auto=format&fit=crop&w=1489&q=80'} 
                      alt={article.title}
                      className="w-full h-full object-cover group-hover:scale-105 transition-transform duration-300"
                      onError={(e) => {
                        e.target.src = 'https://images.unsplash.com/photo-1589578228447-e1a4e481c6c8?ixlib=rb-1.2.1&auto=format&fit=crop&w=1489&q=80';
                      }}
                    />
                  </div>
                  <div className="flex-1">
                    <h4 className="font-medium text-gray-800 mb-1 line-clamp-2 group-hover:text-blue-500 transition-colors">
                      {article.title}
                    </h4>
                    <div className="flex items-center text-sm text-gray-500">
                      <span className="mr-3 text-blue-500">{article.source?.name}</span>
                      <span>•</span>
                      <span className="ml-3">{new Date(article.publishedAt).toLocaleDateString()}</span>
                    </div>
                  </div>
                </a>
              ))}
            </div>
          )}
          
          <a 
            href="#" 
            className="block mt-6 text-center text-blue-500 hover:text-blue-600 font-medium"
          >
            View All News
          </a>
        </div>
      </main>
    </div>
  );
}

// Feature Card Component
function FeatureCard({ title, description, icon, onClick }) {
  const [showDialog, setShowDialog] = useState(false);
  
  const getBackgroundImage = (title) => {
    switch(title) {
      case 'Legal Rights':
        return 'https://images.unsplash.com/photo-1589578527966-fdac0f44566c?ixlib=rb-1.2.1&auto=format&fit=crop&w=1489&q=80';
      case 'Govt Schemes':
        return 'https://images.unsplash.com/photo-1554224155-8d04cb21cd6c?ixlib=rb-1.2.1&auto=format&fit=crop&w=1489&q=80';
      case 'NGO Connect':
        return 'https://images.unsplash.com/photo-1559027615-cd4628902d4a?ixlib=rb-1.2.1&auto=format&fit=crop&w=1489&q=80';
      default:
        return '';
    }
  };

  const getDetailedInfo = (title) => {
    switch(title) {
      case 'Legal Rights':
        return {
          description: "Access comprehensive information about your legal rights and protections under Indian law. Get guidance on:",
          points: [
            "Workplace harassment prevention",
            "Domestic violence protection",
            "Property rights and inheritance",
            "Marriage and divorce laws",
            "Criminal justice support"
          ]
        };
      case 'Govt Schemes':
        return {
          description: "Discover government initiatives designed to support and empower women across India. Learn about:",
          points: [
            "Financial assistance programs",
            "Education scholarships",
            "Healthcare benefits",
            "Employment schemes",
            "Skill development programs"
          ]
        };
      case 'NGO Connect':
        return {
          description: "Connect with verified NGOs providing essential support services to women. Find help with:",
          points: [
            "Emergency assistance",
            "Counseling services",
            "Legal aid",
            "Skill training",
            "Support groups"
          ]
        };
      default:
        return { description: "", points: [] };
    }
  };

  const info = getDetailedInfo(title);
  const bgImage = getBackgroundImage(title);

  return (
    <div 
      className="relative group cursor-pointer"
      onMouseEnter={() => setShowDialog(true)}
      onMouseLeave={() => setShowDialog(false)}
    >
      <div 
        className="h-64 rounded-xl overflow-hidden relative"
        style={{
          backgroundImage: `url(${bgImage})`,
          backgroundSize: 'cover',
          backgroundPosition: 'center'
        }}
      >
        {/* Light blue overlay */}
        <div className="absolute inset-0 bg-blue-500 bg-opacity-60"></div>
        
        {/* Content */}
        <div className="relative h-full p-6 flex flex-col justify-end text-white">
          <div className="mb-4">
            {icon}
          </div>
          <h3 className="text-xl font-semibold mb-2">{title}</h3>
          <p className="text-sm text-white">{description}</p>
        </div>
      </div>

      {/* Hover Dialog */}
      {showDialog && (
        <div className="absolute top-0 left-0 right-0 bg-white rounded-xl shadow-2xl transform transition-all duration-300 z-10">
          <div 
            className="h-32 rounded-t-xl relative"
            style={{
              backgroundImage: `url(${bgImage})`,
              backgroundSize: 'cover',
              backgroundPosition: 'center'
            }}
          >
            <div className="absolute inset-0 bg-blue-500 bg-opacity-70 rounded-t-xl">
              <div className="p-6 text-white">
                <div className="mb-2">{icon}</div>
                <h3 className="text-xl font-semibold">{title}</h3>
              </div>
            </div>
          </div>
          
          <div className="p-6">
            <p className="text-gray-600 mb-4">{info.description}</p>
            <ul className="space-y-2">
              {info.points.map((point, index) => (
                <li key={index} className="flex items-center text-gray-700">
                  <div className="w-2 h-2 bg-blue-500 rounded-full mr-3"></div>
                  {point}
                </li>
              ))}
            </ul>
            <button 
              onClick={onClick}
              className="mt-6 w-full bg-blue-500 text-white py-3 rounded-lg hover:bg-blue-600 transition-colors flex items-center justify-center gap-2"
            >
              <span>Access {title}</span>
              <ChevronRight size={16} />
            </button>
          </div>
        </div>
      )}
    </div>
  );
}

// News Card Component
function NewsCard({ title, description, source, date, url, imageUrl }) {
  const [showDialog, setShowDialog] = useState(false);
  const defaultImage = 'https://images.unsplash.com/photo-1589578228447-e1a4e481c6c8?ixlib=rb-1.2.1&auto=format&fit=crop&w=1489&q=80';

  return (
    <div 
      className="group relative"
      onMouseEnter={() => setShowDialog(true)}
      onMouseLeave={() => setShowDialog(false)}
    >
      <div className="bg-white rounded-xl shadow-sm hover:shadow-md transition-all duration-300 overflow-hidden">
        <div className="aspect-video w-full overflow-hidden">
          <img 
            src={imageUrl || defaultImage} 
            alt={title}
            className="w-full h-full object-cover transform group-hover:scale-105 transition-transform duration-300"
            onError={(e) => {
              e.target.src = defaultImage;
            }}
          />
        </div>
        <div className="p-4">
          <h4 className="font-semibold text-gray-800 line-clamp-2 mb-2 text-lg">
            {title}
          </h4>
          <div className="flex items-center justify-between text-sm text-gray-500">
            <div className="flex items-center gap-2">
              <Globe size={14} />
              <span>{source}</span>
            </div>
            <div className="flex items-center gap-2">
              <Clock size={14} />
              <span>{date}</span>
            </div>
          </div>
        </div>
      </div>

      {/* Hover Dialog */}
      {showDialog && (
        <div className="absolute top-0 left-0 right-0 bg-white rounded-xl shadow-xl transform transition-all duration-300 z-10 p-4">
          <div className="aspect-video w-full mb-4 rounded-lg overflow-hidden">
            <img 
              src={imageUrl || defaultImage} 
              alt={title}
              className="w-full h-full object-cover"
              onError={(e) => {
                e.target.src = defaultImage;
              }}
            />
          </div>
          <h4 className="font-semibold text-gray-800 text-xl mb-3">{title}</h4>
          <p className="text-gray-600 mb-4 line-clamp-3">{description}</p>
          <div className="flex items-center justify-between mb-3">
            <div className="flex items-center gap-2 text-gray-500">
              <Globe size={16} />
              <span>{source}</span>
            </div>
            <div className="flex items-center gap-2 text-gray-500">
              <Clock size={16} />
              <span>{date}</span>
            </div>
          </div>
          <a 
            href={url} 
            target="_blank" 
            rel="noopener noreferrer" 
            className="inline-flex items-center justify-center w-full bg-blue-500 text-white py-2 px-4 rounded-lg hover:bg-blue-600 transition-colors gap-2"
          >
            <span>Read Full Article</span>
            <ExternalLink size={16} />
          </a>
        </div>
      )}
    </div>
  );
}

// Navigation Button Component
function NavButton({ icon, label, active, onClick }) {
  return (
    <button 
      className={`flex flex-col items-center py-2 ${active ? 'text-blue-600' : 'text-gray-500'}`}
      onClick={onClick}
    >
      {icon}
      <span className={`text-xs mt-1 ${active ? 'font-medium' : ''}`}>{label}</span>
      {active && <div className="w-1.5 h-1.5 bg-blue-600 rounded-full mt-1"></div>}
    </button>
  );
}

// Updated fallback news with descriptions and image URLs
const fallbackNews = [
  {
    title: "New Policy Announced to Support Women Entrepreneurs in Rural India",
    description: "The government has launched a comprehensive policy to boost women entrepreneurship in rural areas, offering financial support and training programs.",
    source: { name: "Economic Times" },
    publishedAt: "2025-04-15T09:30:00Z",
    url: "#",
    urlToImage: "https://images.unsplash.com/photo-1509099836639-18ba1795216d?ixlib=rb-1.2.1&auto=format&fit=crop&w=1489&q=80"
  },
  {
    title: "Supreme Court Strengthens Sexual Harassment Laws at Workplace",
    description: "In a landmark judgment, the Supreme Court has introduced stricter guidelines for handling workplace harassment cases, emphasizing swift action and victim protection.",
    source: { name: "Legal Tribune" },
    publishedAt: "2025-04-12T16:20:00Z",
    url: "#",
    urlToImage: "https://images.unsplash.com/photo-1589578228447-e1a4e481c6c8?ixlib=rb-1.2.1&auto=format&fit=crop&w=1489&q=80"
  },
  {
    title: "Government Launches New Women's Safety Initiative",
    description: "A new nationwide initiative focusing on women's safety has been launched, incorporating advanced technology and community participation.",
    source: { name: "National Herald" },
    publishedAt: "2025-04-08T14:30:00Z",
    url: "#",
    urlToImage: "https://images.unsplash.com/photo-1573164713714-d95e436ab8d6?ixlib=rb-1.2.1&auto=format&fit=crop&w=1489&q=80"
  }
];