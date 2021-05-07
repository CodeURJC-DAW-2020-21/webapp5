import { Component } from "@angular/core";

@Component({
    selector: 'webfooter',
    templateUrl: './footer.component.html'
})

export class FooterComponent{
    goToTop(){
        window.scrollTo(0, 0);
    }
}