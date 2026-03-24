import { Injectable } from '@angular/core';

export interface KeystrokeEvent {
  key: string;
  keydownTime: number;
  keyupTime: number;
  dwellTime: number;
}

export interface KeystrokeMetrics {
  dwellTimes: number[];
  flightTimes: number[];
}

@Injectable({
  providedIn: 'root',
})
export class KeystrokeService {
  private keyDownMap = new Map<string, number>();
  private lastKeyUpTime: number | null = null;

  private dwellTimes: number[] = [];
  private flightTimes: number[] = [];

  handleKeyDown(event: KeyboardEvent) {
    const time = performance.now();
    this.keyDownMap.set(event.key, time);
  }
  handleKeyUp(event: KeyboardEvent) {
    const time = performance.now();
    const keyDownTime = this.keyDownMap.get(event.key);

    if (!keyDownTime) return;
    if (event.key.length > 1) return;

    // DWELL TIME
    const dwellTime = Math.round(time - keyDownTime);
    this.dwellTimes.push(dwellTime);

    // FLIGHT TIME
    if (this.lastKeyUpTime != null) {
      const flightTime = time - this.lastKeyUpTime;
      this.flightTimes.push(Math.round(flightTime));
    }
    this.lastKeyUpTime = time;

    // Limpiar
    this.keyDownMap.delete(event.key);

    // debug
    console.log('Key:', event.key);
    console.log('Dwell:', dwellTime);
    console.log('Flight:', this.flightTimes[this.flightTimes.length - 1]);
  }

  getMetrics(): KeystrokeMetrics {
    return {
      dwellTimes: this.dwellTimes,
      flightTimes: this.flightTimes,
    };
  }

  reset() {
    this.keyDownMap.clear();
    this.dwellTimes = [];
    this.flightTimes = [];
    this.lastKeyUpTime = null;
  }
}
