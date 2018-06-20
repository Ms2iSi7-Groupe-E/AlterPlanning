import LanguageSettings = DataTables.LanguageSettings;

export class DatatableFrench {
  public static getLanguages(): LanguageSettings {
    return {
      emptyTable: "Aucune donnée disponible dans le tableau",
      info: "Affichage de l'élément _START_ à _END_ sur _TOTAL_ éléments",
      infoEmpty: "Affichage de l'élément 0 à 0 sur 0 élément",
      infoFiltered: "(filtré de _MAX_ éléments au total)",
      infoPostFix: "",
      lengthMenu: "Afficher _MENU_ éléments",
      loadingRecords: "Chargement en cours...",
      processing: "Traitement en cours...",
      search: "Rechercher :",
      zeroRecords: "Aucun élément à afficher",
      paginate: {
        first: "Premier",
        previous: "Précédent",
        next: "Suivant",
        last: "Dernier"
      },
      aria: {
        sortAscending: ": activer pour trier la colonne par ordre croissant",
        sortDescending: ": activer pour trier la colonne par ordre décroissant"
      }
    };
  }
}
