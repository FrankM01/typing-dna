import { Component } from '@angular/core';
import { KeystrokeService } from '../../services/keystroke.service';

@Component({
  selector: 'app-capture',
  standalone: true,
  templateUrl: './capture.component.html',
  styleUrls: ['./capture.component.scss'],
})
export class CaptureComponent {
  constructor(private keystrokeService: KeystrokeService) {}

  onKeyDown(event: KeyboardEvent) {
    this.keystrokeService.handleKeyDown(event);
  }

  onKeyUp(event: KeyboardEvent) {
    this.keystrokeService.handleKeyUp(event);
  }

  showMetrics() {
    console.log(this.keystrokeService.getMetrics());
  }
}
