package br.com.jrr.apiTest.service.APIConfigService;

import java.util.Scanner;


public class FindApiData {

    private Scanner in = new Scanner(System.in);
    private GetData get = new GetData();
    ConvertData convert = new ConvertData();
    private final String LINK = "https://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=37a01a66";

   // public void findSerie(){
    //  var FindBytitle = in.nextLine();
    //  var json = get.obterDados(LINK + FindBytitle.replace("", "+") + API_KEY);
     //  DataSerie dataSerie = convert.getDate(json, DataSerie.class);
     //   System.out.println(dataSerie);

     //   List<DataSeason> seasons = new ArrayList<>();
      //  	for(int i = 1; i<dataSerie.totalSeasons(); i++){
       // 		json = get.obterDados(LINK + FindBytitle.replace("", "+") +"&season=" + i + API_KEY);
      //          DataSeason dataSeason = convert.getDate(json, DataSeason.class);
       // 		seasons.add(dataSeason);
        //	}
        	//seasons.forEach(System.out::println);

      //  seasons.forEach(t -> t.episodes().forEach(e -> System.out.println(e.title())));
      //  seasons.forEach(System.out::println);

        //top 3
      //  List<DataEpisode> dataEpisodes = seasons.stream()
       //         .flatMap(t -> t.episodes().stream())
     //           .collect(Collectors.toList());

      //  dataEpisodes.stream()
        //        .filter(e -> !e.review().equalsIgnoreCase("N/A"))
        //        .sorted(Comparator.comparing(DataEpisode::review).reversed())
         //       .limit(3)
         //       .forEach(System.out::println);



      //  List<Episode> episodes = seasons.stream()
       //         .flatMap(t -> t.episodes().stream()
      //                  .map(d -> new Episode(t.numero(), d))
      //          ).collect(Collectors.toList());

        //buscar por data
      //  DateTimeFormatter formatar = DateTimeFormatter.ofPattern("dd/mm/yyyy");

        //Modificar depois
     //   var year = 2024;
    //    LocalDate dateFind = LocalDate.of(year, 1, 1);
    //    episodes.stream()
       //         .filter(e -> e.getDateReleased() != null && e.getDateReleased().isAfter(dateFind))
       //         .forEach(e -> System.out.println(
           //             "Season" + e.getSeason() +
       //                         "Episdoe" + e.getTitle() +
                     //           "Date" + e+ e.getDateReleased().format(formatar)
             //   ));



        //buscar episode
       // var trchoTitle

   // }







}
