import { Component, EventEmitter, Input, Output } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Project } from '../../models/project.model';
import { WorkflowItem } from '../../models/workflow.model';

@Component({
  selector: 'app-project-modal',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './project-modal.component.html',
  styleUrls: ['./project-modal.component.css']
})
export class ProjectModalComponent {
  @Input() project: Project | null = null;
  @Input() isOpen = false;
  @Output() close = new EventEmitter<void>();
  @Output() askAboutProject = new EventEmitter<string>();
  @Output() openWorkflow = new EventEmitter<string>();
  @Output() viewWorkflowItem = new EventEmitter<WorkflowItem>();

  onClose(): void {
    this.close.emit();
  }

  onAskAi(): void {
    if (this.project) {
      this.askAboutProject.emit(this.project.askPrompt);
      this.close.emit();
    }
  }

  onOpenWorkflow(imageUrl: string): void {
    this.openWorkflow.emit(imageUrl);
  }

  onSelectWorkflow(wf: WorkflowItem): void {
    this.viewWorkflowItem.emit(wf);
  }

  scrollToWorkflows(): void {
    const el = document.getElementById('workflow-collection');
    if (el) {
      el.scrollIntoView({ behavior: 'smooth', block: 'start' });
    }
  }
}
