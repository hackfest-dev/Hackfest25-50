const ngos = [
    {
      id: 1,
      name: "Azad Foundation",
      location: "Delhi",
      contact: "+91 11 49053796",
      image: "https://media.licdn.com/dms/image/v2/C561BAQGNvzfK7AeCTQ/company-background_10000/company-background_10000/0/1597827465332/azad_foundation_india_cover?e=1739469600&v=beta&t=LLGuplQRfybqy1uHD6b52aU_Y2pBZn4Wpi7x2P73xZ4",
      website: "https://www.azadfoundation.com/",
    },
    {
      id: 2,
      name: "Shanti Sahyog",
      location: "Delhi",
      contact: "+91 8285677777",
      image: "https://scontent.frpr1-2.fna.fbcdn.net/v/t39.30808-1/324873711_493233259646521_3435306574675523820_n.png?stp=dst-png_s200x200&_nc_cat=110&ccb=1-7&_nc_sid=2d3e12&_nc_ohc=NORv943LpwMQ7kNvgHW2gQw&_nc_zt=24&_nc_ht=scontent.frpr1-2.fna&_nc_gid=AVJ6YmVCd0yNxpWMcv7gN-c&oh=00_AYD3Z5yZ2MYtMr02CyasylmTwlvkbD-2wWPW0Nflf0YcAQ&oe=67AACA8D",
      website: "https://shantisahyog.org/",
    },
    {
      id: 3,
      name: "SEWA Delhi",
      location: "Delhi",
      contact: "+91-11-2584-1369",
      image: "https://scontent.frpr1-2.fna.fbcdn.net/v/t39.30808-1/307143471_467736078724278_4748321654040573785_n.jpg?_nc_cat=106&ccb=1-7&_nc_sid=2d3e12&_nc_ohc=VSY6OaVGEFQQ7kNvgHKcGw5&_nc_zt=24&_nc_ht=scontent.frpr1-2.fna&_nc_gid=As05z6TLMXT-jqWjyDqQTAP&oh=00_AYBPtEZN_Y0v61DTXO7ZCOwVDzhdji4cxy9L1pc_Ex589Q&oe=67AAD6C9",
      website: "https://sewadelhi.org/",
    },
    {
      id: 4,
      name: "Nitya Foundation",
      location: "Delhi",
      contact: "919818885691",
      image: "https://www.nityango.org/wp-content/uploads/2023/06/nityalogo.png",
      website: "https://www.nityango.org/",
    },
    {
      id: 5,
      name: "Sneha Foundation",
      location: "Mumbai",
      contact: "91675 35765",
      image: "https://www.snehamumbai.org/wp-content/themes/sneha/img/logo.png",
    },
    {
      id: 6,
      name: "Coro India",
      location: "Mumbai",
      contact: "91-(22)-25295002",
      image: "http://beta.coroindia.org/wp-content/uploads/2023/08/logo.png",
      website: "https://coroindia.org/",
    },
    {
      id: 7,
      name: "Sitaram Jindal Foundation",
      location: "Bengaluru",
      contact: "91-80-2371-7777 / 78 / 79 / 80",
      image: "https://www.sitaramjindalfoundation.org/assets/images/logo.gif",
      website: "https://www.sitaramjindalfoundation.org/",
    },
    {
      id: 8,
      name: "Sampark",
      location: "Bengaluru",
      contact: "91 080 2553 0196",
      image: "https://sp-ao.shortpixel.ai/client/to_auto,q_glossy,ret_img,w_616/https://sampark.org/wp-content/uploads/2023/10/cropped-logo-10-1.png",
      website: "https://sampark.org/",
    },
    {
      id: 9,
      name: "Adithi",
      location: "Patna",
      contact: "+91 8252254574",
      image: "https://adithi.org/files/Adithi-logo.png",
      website: "https://adithi.org/",
    },
    {
      id: 10,
      name: "Women & Child Development Corporation",
      location: "Patna",
      contact: "+91 0612-2506068",
      image: "https://wcdc.bihar.gov.in/images/WDClogo.png?width=331&height=75",
      website: "https://wcdc.bihar.gov.in/",
    },
    {
      id: 121,
      name: "Women Of Worth (WOW)",
      location: "Chennai",
      contact: "+91 44 4264 3036",
      image: "https://womenofworth.in/wp-content/uploads/2019/06/WOW-Logo-01.png",
      website: "https://womenofworth.in/",
    },
    {
      id: 12,
      name: "Red Brigade Trust",
      location: "Lucknow",
      contact: "+91 9455025746",
      image: "https://redbrigade-lucknow.org/uploads/img/logo.png",
      website: "https://redbrigade-lucknow.org/",
    },
    {
      id: 13,
      name: "Sakhi",
      location: "Lucknow",
      contact: "+91 6364363659",
      image: "https://sakhischolars.org/wp-content/uploads/2021/10/Sakhi-Logo-in-Square-1.png",
      website: "https://sakhischolars.org/",
    },
    {
      id: 14,
      name: "Maiti India",
      location: "Kolkata",
      contact: "+91 9650110687",
      image: "https://maitiindia.org/wp-content/uploads/2022/09/MaitiIndiaLogo.png",
      website: "https://maitiindia.org/",
    },
    {
      id: 15,
      name: "Swayam",
      location: "Kolkata",
      contact: "+91 9830079448",
      image: "https://swayam.info/wp-content/themes/swayam/assets/img/swayam-logo.svg",
      website: "https://swayam.info/",
    },
    {
      id: 16,
      name: "Red Dot Foundation",
      location: "Haryana",
      image: "https://reddotfoundation.in/assets/images/logo.png",
      website: "https://reddotfoundation.in/",
    },
    {
      id: 17,
      name: "Youth Helping Trust",
      location: "Delhi",
      contact: "+91 9411948783",
      image: "https://www.youthhelpingtrust.org/wp-content/uploads/2020/10/ylogo.png",
      website: "https://www.youthhelpingtrust.org/our-program/women-empowerment/",
    }
  ];

  export default ngos;