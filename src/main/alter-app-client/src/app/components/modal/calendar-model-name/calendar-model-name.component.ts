import {Component, EventEmitter, Output} from '@angular/core';
import {NgbActiveModal} from "@ng-bootstrap/ng-bootstrap";

@Component({
  selector: 'app-calendar-model-name',
  templateUrl: './calendar-model-name.component.html',
  styleUrls: ['./calendar-model-name.component.scss']
})
export class CalendarModelNameComponent {

  @Output() validate = new EventEmitter<any>();

  name = "";

  constructor(public activeModal: NgbActiveModal) {}

  clickOnValid() {
    this.validate.emit({name: this.name});
    this.activeModal.dismiss('Cross click');
  }
}
