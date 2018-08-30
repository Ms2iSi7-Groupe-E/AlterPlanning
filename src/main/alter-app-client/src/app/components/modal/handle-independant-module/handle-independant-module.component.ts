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

  selectedLibCourt = '';
  selectedLibLong = '';
  selectedDateDebut = null;
  selectedDateFin = null;
  selectedVolumeHor = 0;
  selectedLieu = null;

  constructor(public activeModal: NgbActiveModal,
              private lieuService: LieuService) {}

  ngOnInit() {
    this.lieuService.getLieuxTeachningCourses().subscribe(res => {
      this.lieux = res;
    }, console.error);

    if (this.independantModule !== null) {
      this.selectedLibCourt = this.independantModule.shortName;
      this.selectedLibLong = this.independantModule.longName;
      this.selectedVolumeHor = this.independantModule.hours;
      this.selectedLieu = this.independantModule.codeLieu;

      const dateDeb = new Date(this.independantModule.startDate);
      const dateFin = new Date(this.independantModule.endDate);

      this.selectedDateDebut = {
        year: dateDeb.getFullYear(),
        month: dateDeb.getMonth() + 1,
        day: dateDeb.getDate()
      };

      this.selectedDateFin = {
        year: dateFin.getFullYear(),
        month: dateFin.getMonth() + 1,
        day: dateFin.getDate()
      };
    }
  }

  get title(): string {
    return this.action === ActionTypes.UPDATE ? 'Modification' : 'Création';
  }

  get buttonTitle() {
    return this.action === ActionTypes.UPDATE ? 'Modifier le cours' : 'Creer le cours';
  }

  get libCoursValid() {
    return this.selectedLibCourt !== '' && this.selectedLibCourt.length > 5 && this.selectedLibCourt.length <= 20;
  }

  get libLongValid() {
    return this.selectedLibLong !== '' && this.selectedLibLong.length > 5 && this.selectedLibLong.length <= 200;
  }

  get formValid() {
    return this.libCoursValid && this.libLongValid
      && this.selectedDateDebut != null && this.selectedDateFin != null
      && this.selectedVolumeHor !== null && this.selectedLieu !== null;
  }

  clickOnSave() {
    this.save.emit({
      libelleCourt: this.selectedLibCourt,
      libelleLong: this.selectedLibLong,
      dateDebut: new Date(this.selectedDateDebut.year, this.selectedDateDebut.month - 1, this.selectedDateDebut.day),
      dateFin: new Date(this.selectedDateFin.year, this.selectedDateFin.month - 1, this.selectedDateFin.day),
      lieu: this.selectedLieu,
      volumeHoraire: this.selectedVolumeHor,
    });
    this.activeModal.dismiss('Cross click');
  }
}
