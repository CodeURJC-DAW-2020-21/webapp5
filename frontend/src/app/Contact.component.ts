import  { Component } from '@angular/core';
import { Team } from './model/Team.model';


@Component({
    selector : 'contact',
    templateUrl: './Contact.component.html'
})

export class ContactComponent {

    teams: Team[] = [];
    token?:boolean;

}