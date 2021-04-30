import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { FooterComponent } from './footer.component';
import { HeaderComponent } from './header.component';
import { TournamentsComponent } from './tournaments.component';

@NgModule({
  declarations: [
    AppComponent,
    HeaderComponent, 
    FooterComponent,
    TournamentsComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
