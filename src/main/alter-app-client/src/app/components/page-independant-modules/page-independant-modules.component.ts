import {Component, OnInit, ViewChild} from '@angular/core';
import {NgbModal} from "@ng-bootstrap/ng-bootstrap";
import {HandleIndependantModuleComponent} from "../modal/handle-independant-module/handle-independant-module.component";
import {ActionTypes} from "../../models/enums/action-types";
import {DataTableDirective} from "angular-datatables";
import {Subject} from "rxjs/Subject";
import {DatatableFrench} from "../../helper/datatable-french";
import {IndependantModuleService} from "../../services/independant-module.service";
import {IndependantModuleModel} from "../../models/independant-module.model";
import {ConfirmComponent} from "../modal/confirm/confirm.component";

@Component({
  selector: 'app-page-independant-modules',
  templateUrl: './page-independant-modules.component.html',
  styleUrls: ['./page-independant-modules.component.scss']
})
export class PageIndependantModulesComponent implements OnInit {

  @ViewChild(DataTableDirective)
  dtElement: DataTableDirective;
  dtOptions: DataTables.Settings = {};
  dtTrigger: Subject<void> = new Subject();

  cours = [];

  constructor(private modalService: NgbModal,
              private independantModuleService: IndependantModuleService) {
    this.dtOptions = {
      order: [],
      columnDefs: [{targets: 'no-sort', orderable: false}],
      language: DatatableFrench.getLanguages(),
    };
  }

  ngOnInit() {
    this.independantModuleService.getCours().subscribe(res => {
      this.cours = res;
      this.dtTrigger.next();
    }, console.error);
  }

  createCours() {
    const modalRef = this.modalService.open(HandleIndependantModuleComponent, { size: 'lg' });
    modalRef.componentInstance.action = ActionTypes.CREATE;
    modalRef.componentInstance.save.subscribe(res => {
      const body = new IndependantModuleModel();
      body.codeLieu = res.lieu;
      body.shortName = res.libelleCourt;
      body.longName = res.libelleLong;
      body.startDate = res.dateDebut;
      body.endDate = res.dateFin;
      body.volumeHoraire = res.volumeHoraire;

      this.independantModuleService.addCours(body).subscribe(im => {
        this.cours.push(im);
        this.updateDatatable();
      }, console.error);
    });
  }

  updateCours(cour) {
    const modalRef = this.modalService.open(HandleIndependantModuleComponent, { size: 'lg' });
    modalRef.componentInstance.action = ActionTypes.UPDATE;
    modalRef.componentInstance.independantModule = cour;
    modalRef.componentInstance.save.subscribe(res => {
      const body = new IndependantModuleModel();
      body.codeLieu = res.lieu;
      body.shortName = res.libelleCourt;
      body.longName = res.libelleLong;
      body.startDate = res.dateDebut;
      body.endDate = res.dateFin;
      body.volumeHoraire = res.volumeHoraire;

      this.independantModuleService.updateCoursById(cour.id, body).subscribe(() => {
        cour.codeLieu = res.lieu;
        cour.shortName = res.libelleCourt;
        cour.longName = res.libelleLong;
        cour.startDate = res.dateDebut;
        cour.endDate = res.dateFin;
        cour.hours = res.volumeHoraire;
        this.updateDatatable();
      }, console.error);
    });
  }

  deleteCours(cour) {
    const modalRef = this.modalService.open(ConfirmComponent, { size: 'sm' });
    modalRef.componentInstance.text = "Voulez-vous vraiment supprimer ce cours du module indépendant ?";
    modalRef.componentInstance.validate.subscribe(() => {
      this.independantModuleService.deleteCoursById(cour.id).subscribe(() => {
        this.cours = this.cours.filter(c => c.id !== cour.id);
        this.updateDatatable();
      }, console.error);
    });
  }

  updateDatatable() {
    this.dtElement.dtInstance.then((dtInstance: DataTables.Api) => {
      // Destroy the table first
      dtInstance.destroy();
      // Call the dtTrigger to rerender again
      this.dtTrigger.next();
    });
  }
}
