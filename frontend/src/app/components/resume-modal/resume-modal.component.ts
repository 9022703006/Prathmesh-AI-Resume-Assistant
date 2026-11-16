import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { CommonModule } from '@angular/common';
import { DomSanitizer, SafeResourceUrl } from '@angular/platform-browser';

@Component({
  selector: 'app-resume-modal',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './resume-modal.component.html',
  styleUrls: ['./resume-modal.component.css']
})
export class ResumeModalComponent implements OnInit {
  @Input() isOpen = false;
  @Output() close = new EventEmitter<void>();

  resumePath = 'assets/PrathmeshResume.pdf';
  safePdfUrl!: SafeResourceUrl;

  constructor(private sanitizer: DomSanitizer) {}

  ngOnInit(): void {
    this.safePdfUrl = this.sanitizer.bypassSecurityTrustResourceUrl(this.resumePath);
  }

  onClose(): void {
    this.close.emit();
  }

  downloadResume(): void {
    const link = document.createElement('a');
    link.href = this.resumePath;
    link.download = 'PrathmeshResume.pdf';
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
  }
}
