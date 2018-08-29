import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {NgbActiveModal} from "@ng-bootstrap/ng-bootstrap";
import {ActionTypes} from "../../../models/enums/action-types";

@Component({
  selector: 'app-handle-independant-module',
  templateUrl: './handle-independant-module.component.html',
  styleUrls: ['./handle-independant-module.component.scss']
})
export class HandleIndependantModuleComponent implements OnInit {

  ACTION_TYPE = ActionTypes;

  @Output() save = new EventEmitter<any>();
  @Input() action: ActionTypes = ActionTypes.CREATE;
  @Input() independantModule = null;

  constructor(public activeModal: NgbActiveModal) {}

  ngOnInit() {
    console.log('action', this.action);
    console.log('independantModule', this.independantModule);
  }

  get title(): string {
    return this.action === ActionTypes.UPDATE ? 'Modification' : 'Création';
  }

  clickOnSave() {
    // this.save.emit({name: this.name}); // TODO
    this.activeModal.dismiss('Cross click');
  }
}
