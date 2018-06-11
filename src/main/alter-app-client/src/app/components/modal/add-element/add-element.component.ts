import {Component, EventEmitter, Output} from '@angular/core';
import {NgbActiveModal} from "@ng-bootstrap/ng-bootstrap";

@Component({
  selector: 'app-add-element',
  templateUrl: './add-element.component.html',
  styleUrls: ['./add-element.component.scss']
})
export class AddElementComponent {

  @Output() add = new EventEmitter<any>();

  constructor(public activeModal: NgbActiveModal) {}

  addElement() {
    this.add.emit({
      name: 'FORMATION',
      value: 'MS2I-SI7'
    });
    this.activeModal.dismiss('Cross click');
  }
}
