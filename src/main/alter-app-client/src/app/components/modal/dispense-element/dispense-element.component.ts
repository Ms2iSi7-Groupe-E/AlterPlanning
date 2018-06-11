import {Component, EventEmitter, OnInit, Output} from '@angular/core';
import {NgbActiveModal} from "@ng-bootstrap/ng-bootstrap";

@Component({
  selector: 'app-dispense-element',
  templateUrl: './dispense-element.component.html',
  styleUrls: ['./dispense-element.component.scss']
})
export class DispenseElementComponent implements OnInit {

  @Output() dispense = new EventEmitter<any>();

  constructor(private activeModal: NgbActiveModal) {}

  ngOnInit() {
  }

  dispenseElement() {
    this.dispense.emit({
      name: 'PAS_EN_MEME_TEMPS_QUE',
      value: 211
    });
    this.activeModal.dismiss('Cross click');
  }
}
