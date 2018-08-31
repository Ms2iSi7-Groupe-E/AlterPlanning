import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {NgbActiveModal} from "@ng-bootstrap/ng-bootstrap";

@Component({
  selector: 'app-confirm',
  templateUrl: './confirm.component.html',
  styleUrls: ['./confirm.component.scss']
})
export class ConfirmComponent {

  @Output() validate = new EventEmitter<void>();
  @Input() text = "";

  constructor(public activeModal: NgbActiveModal) {}

  clickOnValid() {
    this.validate.emit();
    this.activeModal.dismiss('Cross click');
  }
}
