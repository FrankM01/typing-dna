import { Routes } from '@angular/router';
import { authGuard } from './core/guards/auth.guards';

export const routes: Routes = [
  {
    path: '',
    redirectTo: 'auth',
    pathMatch: 'full',
  },
  {
    path: 'auth',
    loadChildren: () =>
      import('./features/auth/auth.routes').then((m) => m.AUTH_ROUTES),
  },
  {
    path: 'typing',
    loadChildren: () =>
      import('./features/typing/typing.routes').then((m) => m.TYPING_ROUTES),
    canActivate: [authGuard]
  },
];
