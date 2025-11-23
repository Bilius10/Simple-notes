import { Routes } from '@angular/router';
import { LoginComponent } from './components/login-component/login-component';
import { RegisterComponent } from './components/register-component/register-component';
import { ConfirmEmailComponent } from './components/confirm-email-component/confirm-email-component';
import { ForgotEmailComponent } from './components/forgot-email-component/forgot-email-component';
import { ResetPasswordComponent } from './components/reset-password-component/reset-password-component';
import { MenuComponent } from './components/menu-component/menu-component';
import {BaseComponent} from './components/base-component/base-component';
import {ProfileCardComponent} from './components/profile-card-component/profile-card-component';
import {AddFriendComponent} from './components/add-friend-component/add-friend-component';
import {EditProfileComponent} from './components/edit-profile-component/edit-profile-component';
import {NotifcationComponent} from './components/notifcation-component/notifcation-component';
import {FriendsListComponent} from './components/friends-list-component/friends-list-component';
import {
  PendingFriendRequestsComponent
} from './components/pending-friend-requests-component/pending-friend-requests-component';
import {CreateWalletComponent} from './components/create-wallet-component/create-wallet-component';
import {ViewWalletsComponent} from './components/view-wallets-component/view-wallets-component';
import {ViewWalletComponent} from './components/view-wallet-component/view-wallet-component';
import {AddPermissionComponent} from './components/add-permission-component/add-permission-component';


export const routes: Routes = [

  { path: '', redirectTo: 'auth/login', pathMatch: 'full' },

  { path: 'auth/login', component: LoginComponent },
  { path: 'auth/register', component: RegisterComponent },
  { path: 'confirm-email/:token', component: ConfirmEmailComponent },
  { path: 'forgot-password', component: ForgotEmailComponent },
  { path: 'reset-password/:token', component: ResetPasswordComponent },

  {
    path: '',
    component: BaseComponent,
    children: [
      { path: 'menu', component: MenuComponent },
      { path: 'profile-card', component: ProfileCardComponent},
      { path: 'list-users', component: AddFriendComponent},
      { path: 'edit-profile', component: EditProfileComponent},
      { path: 'notifications', component: NotifcationComponent},
      { path: 'friends', component: FriendsListComponent},
      { path: 'pending', component: PendingFriendRequestsComponent},
      { path: 'create-wallet', component: CreateWalletComponent},
      { path: 'view-wallets', component: ViewWalletsComponent},
      { path: 'wallet/:id', component: ViewWalletComponent},
      { path: 'add-permission', component: AddPermissionComponent}
    ]
  },

  { path: '**', redirectTo: 'auth/login' }
];
