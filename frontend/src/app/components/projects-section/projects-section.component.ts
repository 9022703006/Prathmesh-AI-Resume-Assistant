import { Component, EventEmitter, Output } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Project } from '../../models/project.model';
import { PROJECTS } from '../../data/projects.data';

@Component({
  selector: 'app-projects-section',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './projects-section.component.html',
  styleUrls: ['./projects-section.component.css']
})
export class ProjectsSectionComponent {
  projects: Project[] = PROJECTS;

  @Output() viewDetails = new EventEmitter<Project>();
  @Output() askAboutProject = new EventEmitter<string>();
  @Output() openWorkflow = new EventEmitter<string>();

  onViewDetails(project: Project): void {
    this.viewDetails.emit(project);
  }

  onAskAi(prompt: string, event: Event): void {
    event.stopPropagation();
    this.askAboutProject.emit(prompt);
  }

  onOpenWorkflow(imageUrl: string, event: Event): void {
    event.stopPropagation();
    this.openWorkflow.emit(imageUrl);
  }
}
