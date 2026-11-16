import { WorkflowItem } from '../models/workflow.model';

export const WORKFLOWS_DATA: WorkflowItem[] = [
  {
    id: 'orderflow-ai',
    title: 'OrderFlow AI',
    badge: 'Enterprise AI & ERP',
    icon: '🤖',
    shortTech: 'n8n + Gemini + Spring Boot',
    description:
      'AI-powered order management and workflow automation system connecting n8n with Spring Boot services for order processing and AI-assisted interactions.',
    technologies: ['n8n', 'Gemini', 'Spring Boot', 'REST API', 'PostgreSQL', 'Brevo SMTP'],
    screenshot: 'assets/workflow-images/orderflow-ai.png',
    externalUrl:
      'https://drive.google.com/file/d/1baOTs2UaaMDBxoM_K2cm6yAHpkOrgc_i/view?usp=sharing',
    keySteps: [
      'React.js UI & Webhook Trigger',
      'n8n Orchestration Layer',
      'Spring Boot REST APIs & Order Logic',
      'Google Gemini AI Intent & Conversational Engine',
      'PostgreSQL Persistent Database',
      'Brevo SMTP Automated Email OTP & Notifications'
    ]
  },
  {
    id: 'admission-form',
    title: 'Admission Form Automation',
    badge: 'Education & Routing',
    icon: '🎓',
    shortTech: 'n8n + Google Sheets',
    description:
      'Automated admission form processing using n8n. The workflow collects applicant information, applies conditional processing, and stores the submitted data in different Google Sheets paths based on the workflow condition.',
    technologies: ['n8n', 'Google Sheets', 'n8n Form Trigger', 'Conditional Automation', 'Data Routing'],
    screenshot: 'assets/workflow-images/admission-form.png',
    externalUrl:
      'https://drive.google.com/file/d/1Rn9OcGqzb6BkWsA4z_AwDEdLUXxih6kL/view?usp=sharing',
    keySteps: [
      'n8n Form Trigger (Public Student Application)',
      'Applicant Fields: Full Name, Email ID, Phone Number, Age 18+',
      'IF Conditional Processing Logic (Age & Admission Eligibility)',
      'Google Sheets Destination Paths (Segregated Sheets Routing)',
      'Automated Submission Timestamp (submittedAt)'
    ]
  },
  {
    id: '3d-product-video',
    title: 'AI 3D Product Video Generator',
    badge: 'Generative AI Pipeline',
    icon: '🎬',
    shortTech: 'n8n + Kling AI + API',
    description:
      'AI-powered workflow that accepts a product image and generates a professional 3D rotating product video using an image-to-video AI API.',
    technologies: ['n8n', 'Kling AI', 'FAL API', 'Image-to-Video', 'Form Trigger', 'REST APIs'],
    screenshot: 'assets/workflow-images/3d-product-video.png',
    externalUrl:
      'https://drive.google.com/file/d/1f1qSMn1nszM8x7ciXWPFYQXs0wRetf0k/view?usp=sharing',
    keySteps: [
      'n8n Form Trigger (Product Photo Upload)',
      'Get Image File (Binary Payload Processing)',
      'Create 3D Video via FAL Kling image-to-video AI API',
      'Wait Node (Polling Interval Handler)',
      'Check Video Status API Endpoint',
      'IF Status is IN_PROGRESS (Recursive Verification Loop)',
      'Get Video Link and Download High-Definition Render'
    ]
  }
];
