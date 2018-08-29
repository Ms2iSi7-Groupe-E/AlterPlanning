import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {NgbActiveModal} from "@ng-bootstrap/ng-bootstrap";
import {ActionTypes} from "../../../models/enums/action-types";
import {LieuService} from "../../../services/lieu.service";

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

  lieux = [];

  selectedDateDebut = null;
  selectedDateFin = null;
  selectedLieux = [];

  constructor(public activeModal: NgbActiveModal,
              private lieuService: LieuService) {}

  ngOnInit() {
    this.lieuService.getLieuxTeachningCourses().subscribe(res => {
      this.lieux = res;
    }, console.error);
  }

  get title(): string {
    return this.action === ActionTypes.UPDATE ? 'Modification' : 'Création';
  }

  get button_title() {
    return this.action === ActionTypes.UPDATE ? 'Modifier le cours' : 'Creer le cours';
  }

  clickOnSave() {
    // this.save.emit({name: this.name}); // TODO
    this.activeModal.dismiss('Cross click');
  }
}
