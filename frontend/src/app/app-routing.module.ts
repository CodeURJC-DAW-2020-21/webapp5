import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { TeamsComponent } from './Teams.component';
import { TournamentsComponent } from './tournaments.component';
import { LeagueComponent } from './League.component';
import { ContactComponent } from './Contact.component';
import { LoginComponent } from './Login.component';
import { SettingsComponent } from './Settings.component';
import { HomeComponent } from './Home.component';





const routes: Routes = [
  //{path: '', component: },
  {path: 'new/tournaments', component: TournamentsComponent  },
  {path: 'new/teams',  component: TeamsComponent },
  {path: 'new/leage',  component: LeagueComponent },
  {path: 'new/contact',  component: ContactComponent },
  {path: 'new/login',  component: LoginComponent },
  {path: 'new/settings',  component: SettingsComponent }, 
  {path: 'new/home',  component: HomeComponent }
  
  

 // { path: '', redirectTo: 'tournamemts', pathMatch: 'full' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
