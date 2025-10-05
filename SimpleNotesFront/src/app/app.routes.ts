import { Routes } from '@angular/router';
import {LoginComponent} from './login-component/login-component';
import {RegisterComponent} from './register-component/register-component';
import {ConfirmEmailComponent} from './confirm-email-component/confirm-email-component';
import {ForgotEmailComponent} from './forgot-email-component/forgot-email-component';
import {ResetPasswordComponent} from './reset-password-component/reset-password-component';

export const routes: Routes = [
  {path: '', redirectTo: 'auth/login', pathMatch: 'full'},
  {path: 'auth/login', component: LoginComponent},
  {path: 'auth/register', component: RegisterComponent},
  {path: 'confirm-email/:token', component: ConfirmEmailComponent},
  {path: 'forgot-password', component: ForgotEmailComponent},
  {path: 'reset-password/:token', component: ResetPasswordComponent}
];
