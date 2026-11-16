import { WorkflowItem } from './workflow.model';

export interface Project {
  id: string;
  name: string;
  subtitle?: string;
  category: string;
  shortDescription: string;
  description: string;
  problemSolved: string;
  technologies: string[];
  features: string[];
  architecture?: string;
  emailWorkflow?: string;
  n8nWorkflowDescription?: string;
  n8nWorkflowImage?: string;
  cardImage?: string;
  workflows?: WorkflowItem[];
  highlights: string[];
  liveUrl?: string;
  liveUrlLabel?: string;
  githubUrl?: string;
  githubUrlLabel?: string;
  askPrompt: string;
}
