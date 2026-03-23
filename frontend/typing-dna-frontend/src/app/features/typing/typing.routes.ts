import { Routes } from '@angular/router';

export const TYPING_ROUTES: Routes = [
  {
    path: '',
    loadComponent: () =>
      import('./pages/capture/capture.component').then(
        (m) => m.CaptureComponent,
      ),
  },
];
