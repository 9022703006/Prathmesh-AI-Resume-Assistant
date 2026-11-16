import {
  Component,
  ElementRef,
  ViewChild,
  AfterViewChecked,
  HostListener
} from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { DomSanitizer, SafeHtml } from '@angular/platform-browser';
import { ChatService } from '../services/chat.service';
import { Project } from '../models/project.model';
import { PROJECTS } from '../data/projects.data';
import { WorkflowItem } from '../models/workflow.model';
import { ResumeModalComponent } from '../components/resume-modal/resume-modal.component';
import { ProjectModalComponent } from '../components/project-modal/project-modal.component';
import { ProjectsSectionComponent } from '../components/projects-section/projects-section.component';
import { WorkflowModalComponent } from '../components/workflow-modal/workflow-modal.component';

export interface ChatMessage {
  id: string;
  sender: 'user' | 'ai';
  text: string;
  formattedText?: SafeHtml;
  timestamp: Date;
  showResumeCard?: boolean;
}

@Component({
  selector: 'app-chat',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    ResumeModalComponent,
    ProjectModalComponent,
    ProjectsSectionComponent,
    WorkflowModalComponent
  ],
  templateUrl: './chat.component.html',
  styleUrls: ['./chat.component.css']
})
export class ChatComponent implements AfterViewChecked {
  @ViewChild('messagesContainer') private messagesContainer!: ElementRef<HTMLDivElement>;

  activeTab: 'chat' | 'projects' = 'chat';
  readonly projectsCount = PROJECTS.length;

  userMessage = '';
  loading = false;
  messages: ChatMessage[] = [];

  // Resume Modal State
  isResumeModalOpen = false;

  // Project Modal State
  isProjectModalOpen = false;
  selectedProject: Project | null = null;

  // Individual Workflow Modal State
  isWorkflowModalOpen = false;
  selectedWorkflow: WorkflowItem | null = null;

  // Workflow Viewer (Lightbox) State
  isWorkflowViewerOpen = false;
  workflowImageUrl = '';
  workflowTitle = 'OrderFlow_AI – n8n Workflow Automation';

  // 8 Specific Suggested Questions
  readonly suggestedQuestions: string[] = [
    'Tell me about Prathmesh.',
    'What are his technical skills?',
    'Explain his work experience.',
    'What projects has he developed?',
    'Tell me about Commodity_Admin_AI.',
    'What AI technologies does he know?',
    'Show me his resume.',
    'Share his portfolio and GitHub links.'
  ];

  // Professional Links
  readonly links = {
    portfolio: 'https://personal-portfolio-eltm.onrender.com',
    github: 'https://github.com/9022703006?tab=repositories',
    githubRepositories: 'https://github.com/9022703006?tab=repositories',
    linkedin: 'https://linkedin.com/in/prathmesh-patil-64b1b72a1'
  };

  private shouldScroll = false;

  constructor(
    private chatService: ChatService,
    private sanitizer: DomSanitizer
  ) {}

  ngAfterViewChecked(): void {
    if (this.shouldScroll) {
      this.scrollToBottom();
      this.shouldScroll = false;
    }
  }

  sendMessage(): void {
    const message = this.userMessage.trim();

    if (!message || this.loading) {
      return;
    }

    const isResume = this.isResumeQuery(message);

    // Add user message
    this.messages.push({
      id: 'msg-' + Date.now() + '-user',
      sender: 'user',
      text: message,
      formattedText: this.formatMessage(message),
      timestamp: new Date()
    });

    // Clear input
    this.userMessage = '';
    this.loading = true;
    this.shouldScroll = true;

    // Send using existing API service
    this.chatService.sendMessage(message).subscribe({
      next: (response: string) => {
        const responseHasResume = isResume || this.isResumeQuery(response);

        this.messages.push({
          id: 'msg-' + Date.now() + '-ai',
          sender: 'ai',
          text: response,
          formattedText: this.formatMessage(response),
          timestamp: new Date(),
          showResumeCard: responseHasResume
        });

        this.loading = false;
        this.shouldScroll = true;
      },
      error: (error) => {
        console.error('Chat API Error:', error);

        const fallbackText = isResume
          ? "Here is Prathmesh's official resume. You can view or download the complete PDF using the card below."
          : 'Sorry, I encountered a temporary issue connecting to the AI backend. Please try asking again in a moment.';

        this.messages.push({
          id: 'msg-' + Date.now() + '-ai-err',
          sender: 'ai',
          text: fallbackText,
          formattedText: this.formatMessage(fallbackText),
          timestamp: new Date(),
          showResumeCard: isResume
        });

        this.loading = false;
        this.shouldScroll = true;
      }
    });
  }

  sendSuggestedQuestion(question: string): void {
    if (this.loading) {
      return;
    }
    this.activeTab = 'chat';
    this.userMessage = question;
    this.sendMessage();
  }

  clearChat(): void {
    this.messages = [];
    this.userMessage = '';
  }

  openResumeModal(): void {
    this.isResumeModalOpen = true;
  }

  closeResumeModal(): void {
    this.isResumeModalOpen = false;
  }

  downloadResume(): void {
    const link = document.createElement('a');
    link.href = 'assets/PrathmeshResume.pdf';
    link.download = 'PrathmeshResume.pdf';
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
  }

  openProjectModal(project: Project): void {
    this.selectedProject = project;
    this.isProjectModalOpen = true;
  }

  closeProjectModal(): void {
    this.isProjectModalOpen = false;
    this.selectedProject = null;
  }

  onAskAboutProject(prompt: string): void {
    this.activeTab = 'chat';
    this.sendSuggestedQuestion(prompt);
  }

  openWorkflowViewer(imageUrl: string, title?: string): void {
    this.workflowImageUrl = imageUrl;
    if (title) {
      this.workflowTitle = title;
    }
    this.isWorkflowViewerOpen = true;
  }

  closeWorkflowViewer(): void {
    this.isWorkflowViewerOpen = false;
  }

  openWorkflowModal(workflow: WorkflowItem): void {
    this.selectedWorkflow = workflow;
    this.isWorkflowModalOpen = true;
  }

  closeWorkflowModal(): void {
    this.isWorkflowModalOpen = false;
    this.selectedWorkflow = null;
  }

  @HostListener('document:keydown.escape')
  onEscapeKey(): void {
    if (this.isWorkflowViewerOpen) {
      this.closeWorkflowViewer();
    } else if (this.isWorkflowModalOpen) {
      this.closeWorkflowModal();
    } else if (this.isProjectModalOpen) {
      this.closeProjectModal();
    } else if (this.isResumeModalOpen) {
      this.closeResumeModal();
    }
  }

  setTab(tab: 'chat' | 'projects'): void {
    this.activeTab = tab;
    if (tab === 'chat') {
      this.shouldScroll = true;
    }
  }

  private isResumeQuery(text: string): boolean {
    if (!text) return false;
    const lower = text.toLowerCase();
    return (
      lower.includes('resume') ||
      lower.includes('cv') ||
      lower.includes('curriculum vitae')
    );
  }

  private scrollToBottom(): void {
    try {
      if (this.messagesContainer) {
        this.messagesContainer.nativeElement.scrollTop =
          this.messagesContainer.nativeElement.scrollHeight;
      }
    } catch (err) {
      // ignore
    }
  }

  private formatMessage(text: string): SafeHtml {
    if (!text) {
      return this.sanitizer.bypassSecurityTrustHtml('');
    }

    // 1. Escape HTML special characters
    let html = text
      .replace(/&/g, '&amp;')
      .replace(/</g, '&lt;')
      .replace(/>/g, '&gt;');

    // 2. Markdown links: [text](https://url)
    html = html.replace(
      /\[([^\]]+)\]\((https?:\/\/[^\s\)]+)\)/g,
      '<a href="$2" target="_blank" rel="noopener noreferrer" class="chat-link">$1</a>'
    );

    // 3. Raw URLs: https://... or http://...
    html = html.replace(
      /(^|[\s(])(https?:\/\/[^\s\)<>]+)/g,
      '$1<a href="$2" target="_blank" rel="noopener noreferrer" class="chat-link">$2</a>'
    );

    // 4. Bold: **text**
    html = html.replace(/\*\*([^*]+)\*\*/g, '<strong>$1</strong>');

    // 5. Bullet points: * or - at line start
    html = html.replace(/(?:^|\n)\s*[*•-]\s+([^\n]+)/g, '<br>• $1');

    // 6. Line breaks
    html = html.replace(/\n/g, '<br>');

    // Clean leading <br> if created by first bullet
    html = html.replace(/^<br>/, '');

    return this.sanitizer.bypassSecurityTrustHtml(html);
  }
}
