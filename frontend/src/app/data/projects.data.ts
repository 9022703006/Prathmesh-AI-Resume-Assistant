import { Project } from '../models/project.model';
import { WORKFLOWS_DATA } from './workflows.data';

export const PROJECTS: Project[] = [
  {
    id: 'personal-portfolio-website',
    name: 'Personal Portfolio Website',
    category: 'FULL STACK / PORTFOLIO',
    cardImage: 'assets/workflow-images/Portfolio.png',
    shortDescription:
      'A full-stack personal portfolio application developed to present my complete professional and academic profile through a single web platform.',
    description:
      'A full-stack personal portfolio application developed to present my complete professional and academic profile through a single web platform. It centralizes personal details, academic credentials, technical skills, and featured software development projects with live demo access.',
    problemSolved:
      'Recruiters and hiring managers often have to search fragmented resumes and multiple profile links to evaluate a candidate. This full-stack application provides a unified, structured showcase of professional experience, education, technical skills, and project portfolios in one seamless interface.',
    technologies: [
      'Angular',
      'Java',
      'Spring Boot',
      'Spring Security',
      'MySQL',
      'REST APIs'
    ],
    features: [
      'Personal details and developer profile presentation',
      'School and college education information and credentials',
      'Comprehensive technical skills and technology stack inventory',
      'Software development projects with architecture and details',
      'Project descriptions and portfolio showcase with external links',
      'Backend REST APIs developed with Spring Boot and secured with Spring Security'
    ],
    architecture:
      'Angular Frontend → Spring Boot REST API → Spring Security → MySQL Database',
    highlights: [
      'Unified complete professional and academic portfolio into a responsive single-page web app',
      'Designed modular RESTful endpoints with Spring Boot and Spring Security',
      'Integrated MySQL database for managing dynamic portfolio content'
    ],
    liveUrl: 'https://personal-portfolio-eltm.onrender.com/',
    liveUrlLabel: 'View Project',
    githubUrl: 'https://github.com/9022703006?tab=repositories',
    githubUrlLabel: 'GitHub Repository',
    askPrompt: 'Tell me about the Personal Portfolio Website project.'
  },
  {
    id: 'ai-b2b-commodity-marketplace-assistant',
    name: 'AI-Powered B2B Commodity Marketplace Assistant',
    subtitle: 'Commodity_Admin_AI',
    category: 'AI-POWERED B2B MARKETPLACE ASSISTANT',
    cardImage: 'assets/workflow-images/B2B_Commodity .png',
    shortDescription:
      'An AI-powered administrative assistant for a B2B commodity raw material marketplace, providing conversational access to marketplace data, RAG-based knowledge retrieval, and real-time commodity and mandi price information.',
    description:
      'Commodity_Admin_AI is an AI-powered administrative assistant developed for a B2B commodity raw material marketplace. It enables marketplace administrators to interact with complex trading and market information through a conversational AI interface.\n\nThe system integrates with backend marketplace APIs to retrieve real-time operational information, including commodity details, supplier inventory, bids, negotiations, and deals. It also uses Retrieval-Augmented Generation (RAG) with PostgreSQL and PGVector to provide context-aware answers about commodity-related knowledge.\n\nAdditionally, the platform provides access to official government mandi prices through the AGMARKNET data source.',
    problemSolved:
      'Managing a high-volume B2B commodity marketplace requires administrators to navigate multiple screens and data sources to track commodity prices, supplier inventory, bids, negotiations, and trading activities.\n\nCommodity_Admin_AI simplifies these operations by introducing a centralized conversational AI interface. Administrators can ask questions in natural language instead of manually searching through multiple dashboards and backend systems.',
    technologies: [
      'Java 17',
      'Spring Boot',
      'Spring AI',
      'Google Gemini 2.5 Flash',
      'PostgreSQL',
      'PGVector',
      'React.js',
      'REST APIs',
      'Maven'
    ],
    features: [
      'AI-powered administrative chatbot for B2B marketplace operations.',
      'Natural-language querying for commodities, supplier inventory, bids, negotiations, and deals.',
      'Spring Boot REST API integration with the existing TradeX backend.',
      'Retrieval-Augmented Generation (RAG) using Spring AI, Google Gemini embeddings, PostgreSQL, and PGVector.',
      'Semantic search using vector embeddings and similarity-based document retrieval.',
      'Live marketplace data retrieval through backend APIs without allowing the AI model to directly access the business database.',
      'Separate Shop Rate and Global Mandi Rate modules with strict data-source isolation.',
      'Official government mandi price integration using data.gov.in (AGMARKNET).',
      'External web-based reference price fallback when official government records are unavailable.',
      'Interactive mandi rate dashboard with commodity, state, district, APMC, and date filters.',
      'Intent-based query processing for different marketplace operations.',
      'Multilingual conversational interaction supporting English, Hindi, and Marathi.'
    ],
    architecture:
      'React Admin Chat Interface → Spring Boot REST API → Intent Detection → Business Services → TradeX Backend APIs / PostgreSQL PGVector → Gemini AI → Conversational Response',
    highlights: [
      'Implemented an AI-powered chatbot using Spring AI and Google Gemini 2.5 Flash.',
      'Integrated PGVector with PostgreSQL for semantic document retrieval and RAG-based responses.',
      'Used REST API-based integration to retrieve marketplace operational data from the existing backend.',
      'Designed intent detection to identify user queries and route them to appropriate business services.',
      'Maintained strict separation between manually managed Shop Rates and official Global Mandi Rates.',
      'Integrated government market-price data with an independent external reference-price fallback mechanism.',
      'Built a React-based administrative interface for conversational marketplace management.'
    ],
    liveUrl: 'https://commodity-ui-m42u.onrender.com/',
    liveUrlLabel: 'View Project',
    githubUrl: 'https://github.com/orgs/TradeX-Marketplace/people/9022703006',
    githubUrlLabel: 'GitHub Repository',
    askPrompt: 'Tell me about the AI-Powered B2B Commodity Marketplace Assistant.'
  },
  {
    id: 'ai-workflow-automation-hub',
    name: 'AI Workflow Automation Hub',
    subtitle: 'Multiple AI-powered automation workflows built with n8n',
    category: 'N8N WORKFLOW AUTOMATION / AI',
    cardImage: 'assets/workflow-images/orderflow-ai.png',
    shortDescription:
      'A dedicated portfolio showcase of multi-step AI and business automation workflows built using n8n, Google Gemini, Kling AI, and Google Sheets.',
    description:
      'AI Workflow Automation Hub is a centralized showcase of production-grade automation workflows orchestrated with n8n. It demonstrates how autonomous AI agents, API integrations, conditional routing, and generative media can be combined to streamline business operations and eliminate manual workflows.\n\nThe hub features full-stack order management integration with Spring Boot and Gemini AI, automated student admissions processing with conditional Google Sheets routing, and generative 3D product video synthesis using image-to-video AI pipelines.',
    problemSolved:
      'Modern businesses waste countless hours on repetitive manual operations—from cross-system data entry and applicant sorting to manual media creation. AI Workflow Automation Hub solves these inefficiencies with autonomous, event-driven n8n workflows that orchestrate AI models and cloud APIs reliably.',
    technologies: [
      'n8n',
      'Google Gemini',
      'Spring Boot',
      'Kling AI',
      'Google Sheets',
      'FAL API',
      'PostgreSQL',
      'REST APIs',
      'Brevo SMTP'
    ],
    features: [
      'OrderFlow AI: End-to-end order orchestration connecting React, Spring Boot, PostgreSQL, and Google Gemini AI.',
      'Admission Form Automation: Automated applicant intake with IF conditional logic routing submissions into segregated Google Sheets.',
      'AI 3D Product Video Generator: Autonomous media pipeline transforming uploaded 2D product photos into 3D rotating videos using FAL Kling AI.',
      'Webhook & Form-driven execution triggers with zero manual intervention required.',
      'Robust error handling, status polling loops, and conditional decision branches in n8n.'
    ],
    architecture:
      'n8n Form Triggers / Webhooks → Conditional Routing & Polling → External AI APIs (Gemini, Kling) & Business Services (Spring Boot, Sheets) → Automated Outputs',
    highlights: [
      'Engineered multi-stage n8n workflows integrating LLMs, multimodal AI, and business APIs',
      'Eliminated manual spreadsheet data entry with conditional automated routing',
      'Automated high-fidelity 3D video creation from raw product image uploads'
    ],
    workflows: WORKFLOWS_DATA,
    githubUrl: 'https://github.com/9022703006?tab=repositories',
    githubUrlLabel: 'GitHub Repository',
    askPrompt:
      'Tell me about the AI Workflow Automation Hub and its n8n workflows.'
  },
  {
    id: 'orderflow-ai',
    name: 'AI-Powered Order Management & Workflow Automation System',
    subtitle: 'OrderFlow_AI',
    category: 'FULL STACK / AI & WORKFLOW AUTOMATION',
    cardImage: 'assets/workflow-images/order.png',
    shortDescription:
      'An AI-powered order management platform combining React.js, Spring Boot, Spring AI, PostgreSQL, and n8n workflow automation to simplify order processing and business communication.',
    description:
      'OrderFlow AI is an AI-powered order management platform designed to simplify order processing and automate business workflows through intelligent AI integration.\n\nThe application provides user registration, email OTP verification, authentication, order management, and an AI-powered assistant. It integrates React.js with Spring Boot backend services and n8n workflows to process user requests and automate communication.\n\nThe AI assistant connects with backend services to retrieve relevant information and support conversational interactions, while PostgreSQL manages application data.',
    problemSolved:
      'Traditional order management systems often require users to manually manage orders, navigate multiple application screens, and handle repetitive communication tasks.\n\nOrderFlow AI addresses these challenges by combining centralized order management with AI-assisted interactions and workflow automation. It simplifies order-related operations, reduces repetitive manual work, and provides a more efficient user experience.',
    technologies: [
      'Java 17',
      'Spring Boot',
      'Spring AI',
      'React.js',
      'PostgreSQL',
      'n8n',
      'Spring Security',
      'REST APIs',
      'Brevo SMTP',
      'Maven'
    ],
    features: [
      'User registration and login with email OTP verification.',
      'Secure authentication and authorization using Spring Security.',
      'User-friendly React.js dashboard for managing orders.',
      'Order creation and management through backend REST APIs.',
      'AI-powered conversational assistant integrated with n8n workflows.',
      'Spring AI integration for connecting AI capabilities with backend services.',
      'Automated workflow execution using n8n.',
      'PostgreSQL database integration for persistent application data.',
      'Email OTP delivery and email communication using Brevo SMTP.',
      'Separation of frontend, backend business logic, and AI workflow orchestration.'
    ],
    architecture:
      'React.js Frontend → n8n Workflow Automation → Spring Boot REST APIs → Business Logic → PostgreSQL Database → AI-Powered Response',
    emailWorkflow:
      'Spring Boot → Brevo SMTP → Email OTP / Automated Email Communication',
    n8nWorkflowDescription:
      'n8n is used as the workflow orchestration layer to connect the AI assistant, Spring Boot APIs, database-backed services, Google Gemini, chat memory, and automated order processing.',
    n8nWorkflowImage: 'assets/orderflow-ai-n8n-workflow.png',
    highlights: [
      'Developed a full-stack application using React.js and Java Spring Boot.',
      'Integrated n8n for AI workflow orchestration and automation.',
      'Connected AI workflows with Spring Boot backend REST APIs.',
      'Implemented email OTP-based user registration and authentication.',
      'Integrated Brevo SMTP for email delivery.',
      'Used PostgreSQL for persistent data storage.',
      'Designed an architecture combining traditional backend services with AI-powered interactions.'
    ],
    liveUrl: 'https://orderflow-ai-frontend.onrender.com/',
    liveUrlLabel: 'View Project',
    githubUrl: 'https://github.com/9022703006/orderflow-ai',
    githubUrlLabel: 'GitHub Repository',
    askPrompt:
      'Tell me about the AI-Powered Order Management & Workflow Automation System (OrderFlow_AI).'
  },
  {
    id: 'commodity-admin-ai',
    name: 'Prathmesh – AI Resume Assistant',
    category: 'AI-Powered Resume Assistant',
    cardImage: 'assets/workflow-images/Resume_chat.png',
    shortDescription:
      'An AI-powered administrative chatbot for a B2B commodity raw material marketplace. It retrieves marketplace information through TradeX APIs and uses RAG for knowledge retrieval.',
    description:
      'An enterprise AI-powered administrative chatbot designed specifically for a B2B commodity raw material marketplace. It empowers marketplace administrators to query operational data including live inventory, supplier bids, active negotiations, and contracts using conversational natural-language queries.',
    problemSolved:
      'In high-volume B2B commodity raw material trading, administrators face high operational overhead navigating multiple fragmented screens to verify inventory rates, ongoing counter-bids, and deal statuses. Commodity_Admin_AI centralizes queries into an intuitive multilingual conversational interface while preserving strict enterprise security and data integrity.',
    technologies: [
      'Java',
      'Spring Boot',
      'Spring AI',
      'Gemini API',
      'PostgreSQL',
      'PGVector',
      'Angular',
      'n8n',
      'REST APIs'
    ],
    features: [
      'Admin-focused conversational AI chatbot for marketplace operations',
      'Natural-language querying for commodity information, inventory, supplier bids, and deals',
      'Secure integration with TradeX backend microservices through REST APIs',
      'Retrieval-Augmented Generation (RAG) powered by PostgreSQL and PGVector for domain knowledge',
      'Enterprise security boundary: The AI model does not query the database directly; all access is mediated by verified business APIs',
      'Strict separation between Shop Rate and Global Rate data sources with zero cross-contamination',
      'Multilingual query processing supporting English, Hindi, and Marathi'
    ],
    architecture:
      'Angular Chat Interface → Spring Boot REST API → Intent Detection → Business Services → TradeX APIs / PGVector → Gemini AI',
    highlights: [
      'Engineered a secure API boundary ensuring AI models never make direct database queries',
      'Sub-second knowledge retrieval with vector embeddings in PostgreSQL & PGVector',
      'Trilingual interaction support for English, Hindi, and Marathi administrators'
    ],
    githubUrl: 'https://github.com/orgs/TradeX-Marketplace/repositories',
    askPrompt: 'Tell me about Prathmesh – AI Resume Assistant.'
  },
  {
    id: 'loan-management-system',
    name: 'Loan Management System with AI Integration',
    category: 'Full Stack / Microservices',
    shortDescription:
      'A role-based loan management platform for customer registration, loan processing and approvals, with AI chatbot integration.',
    description:
      'A scalable enterprise banking microservices platform engineered to handle end-to-end loan application lifecycles. It automates customer registration, document vetting, credit risk assessment, and approval workflows, complemented by an intelligent conversational assistant.',
    problemSolved:
      'Traditional banking loan operations suffer from slow processing cycles, repetitive manual data entry, and delayed applicant communications. This system digitizes and automates the workflow with role-based access and real-time AI guidance.',
    technologies: [
      'Java',
      'Spring Boot',
      'Spring Security',
      'Spring Data JPA',
      'Hibernate',
      'MySQL',
      'Angular',
      'JWT',
      'OpenAI API',
      'n8n',
      'REST APIs',
      'Microservices'
    ],
    features: [
      'Engineered 20+ secure RESTful microservice APIs using Spring Boot and Hibernate',
      'Configured Spring Security with JWT-based role-based access control (Admin, Loan Officer, Customer)',
      'Automated approximately 40% of manual data entry workflows for customer registration and approval vetting',
      'Integrated conversational AI chatbot using OpenAI API and n8n for real-time customer assistance',
      'Optimized database queries and transaction pipelines to ensure high throughput'
    ],
    architecture:
      'Microservices Architecture (Auth Service, Customer Service, Loan Service, Notification Engine) → MySQL DB → Angular Client',
    highlights: [
      'Optimized database queries, improving API response time by approximately 30%',
      'Automated ~40% of manual data entry for registration and loan approvals',
      'Robust JWT authentication with multi-tier role enforcement'
    ],
    githubUrl:
      'https://github.com/9022703006/Loan_Management_System_-Bankend-Java-SpringBoot-',
    askPrompt: 'Explain the Loan Management System project.'
  },
  {
    id: 'livestock-farm-management',
    name: 'AI-Enabled Livestock & Cow Farm Management Platform',
    category: 'MERN Stack / AI Integration',
    cardImage: 'assets/workflow-images/Cow.png',
    shortDescription:
      'A livestock and farm management platform for livestock tracking, farm activity logs and daily operations, with AI chatbot assistance.',
    description:
      'A full-stack agricultural management platform that modernizes livestock operations. It centralizes individual animal tracking, medical and vaccination histories, feed supplies, breeding events, and daily farm logs, augmented with an AI assistant for farmer guidance.',
    problemSolved:
      'Livestock and dairy farms often rely on manual paper records and disjointed notes, leading to delayed medical interventions, missed breeding/vaccination windows, and inefficient feed stock management.',
    technologies: [
      'React.js',
      'Node.js',
      'Express.js',
      'MongoDB',
      'MySQL',
      'n8n',
      'Gemini API',
      'OpenAI API',
      'REST APIs'
    ],
    features: [
      'Developed responsive full-stack MERN platform with intuitive operational dashboards',
      'Built high-performance REST APIs using Node.js and Express.js',
      'Centralized and managed more than 100 livestock records in MongoDB with schema validation',
      'Integrated AI chatbot using n8n with Gemini and OpenAI APIs for farm health advisory',
      'Digitized vaccination logs, feed allocations, and daily activity logs'
    ],
    architecture:
      'React.js UI → Node.js / Express REST APIs → MongoDB & MySQL Data Store → n8n Workflow Engine → Gemini & OpenAI APIs',
    highlights: [
      'Successfully digitized farm management workflows, eliminating manual paper tracking',
      'Improved farm operational efficiency by approximately 50%',
      'Managed 100+ active livestock records and medical histories in MongoDB'
    ],
    liveUrl: 'https://e-commerance-farmmarts.onrender.com/',
    liveUrlLabel: 'View Project',
    githubUrl: 'https://github.com/9022703006/E-commerance_FarmMart',
    githubUrlLabel: 'GitHub Repository',
    askPrompt:
      'Tell me about the AI-Enabled Livestock & Cow Farm Management Platform.'
  }
];
