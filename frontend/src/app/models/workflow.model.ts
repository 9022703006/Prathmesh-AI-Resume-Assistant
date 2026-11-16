export interface WorkflowItem {
  id: string;
  title: string;
  badge?: string;
  icon: string;
  shortTech: string;
  description: string;
  technologies: string[];
  screenshot: string;
  externalUrl: string;
  keySteps?: string[];
}
