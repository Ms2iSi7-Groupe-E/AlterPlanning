import LanguageSettings = DataTables.LanguageSettings;

export class DatatableFrench implements LanguageSettings {
  emptyTable: "Aucune donn&eacute;e disponible dans le tableau";
  info: "Affichage de l'&eacute;l&eacute;ment _START_ &agrave; _END_ sur _TOTAL_ &eacute;l&eacute;ments";
  infoEmpty: "Affichage de l'&eacute;l&eacute;ment 0 &agrave; 0 sur 0 &eacute;l&eacute;ment";
  infoFiltered: "(filtr&eacute; de _MAX_ &eacute;l&eacute;ments au total)";
  infoPostFix: "";
  lengthMenu: "Afficher _MENU_ &eacute;l&eacute;ments";
  loadingRecords: "Chargement en cours...";
  processing: "Traitement en cours...";
  search: "Rechercher&nbsp;:";
  zeroRecords: "Aucun &eacute;l&eacute;ment &agrave; afficher";
  paginate: {
    first: "Premier";
    previous: "Pr&eacute;c&eacute;dent";
    next: "Suivant";
    last: "Dernier"
  };
  aria: {
    sortAscending: ": activer pour trier la colonne par ordre croissant";
    sortDescending: ": activer pour trier la colonne par ordre d&eacute;croissant"
  };
}
