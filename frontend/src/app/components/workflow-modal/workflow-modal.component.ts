import { Component, EventEmitter, HostListener, Input, Output } from '@angular/core';
import { CommonModule } from '@angular/common';
import { WorkflowItem } from '../../models/workflow.model';

@Component({
  selector: 'app-workflow-modal',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './workflow-modal.component.html',
  styleUrls: ['./workflow-modal.component.css']
})
export class WorkflowModalComponent {
  @Input() workflow: WorkflowItem | null = null;
  @Input() isOpen = false;

  @Output() close = new EventEmitter<void>();
  @Output() zoomImage = new EventEmitter<string>();

  @HostListener('document:keydown.escape', ['$event'])
  onEscapeHandler(event: KeyboardEvent): void {
    if (this.isOpen) {
      event.preventDefault();
      this.onClose();
    }
  }

  onClose(): void {
    this.close.emit();
  }

  onZoom(imageSrc: string): void {
    this.zoomImage.emit(imageSrc);
  }
}
