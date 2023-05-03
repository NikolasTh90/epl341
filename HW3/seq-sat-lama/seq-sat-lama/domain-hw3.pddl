(define (domain hw3)
  (:requirements :strips :typing :negative-preconditions :disjunctive-preconditions :conditional-effects :equality)
  (:types

    train-station - location
    harbor - location
    warehouse - location
    truck - vehicle
    ship - vehicle
    wagon - vehicle
    train_machine package city country

  )
  (:predicates
    ; this is weird
    (at ?o - object ?l - location)
    (in_city ?l - location ?c - city)
    (in_country ?c - city ?cn - country)
    (in_vehicle ?p - package ?v - vehicle)
    (is_loaded_vehicle ?v - vehicle)
    (connected_to_train ?w - wagon ?t - train_machine)
    (is_ship-vehicle ?v - vehicle)
  )

  (:action load_vehicle
    :parameters (?p - package ?v - vehicle)
    :precondition (and
      (not (is_ship-vehicle ?v)) ; check that the vehicle is not a ship
      (forall ; check that the package and the vehicle are in the same location
        (?l - location)
        (imply
          (at ?p ?l)
          (at ?v ?l)
        )
      )
      (not (in_vehicle ?p ?v)) ; check that the package is not already in the vehicle
      (not (is_loaded_vehicle ?v)) ; check that the vehicle does not already carry a package
      
    )
    :effect (and
      (in_vehicle ?p ?v) ; put the package in the vehicle
      (is_loaded_vehicle ?v) ; set the vehicle as loaded with a package
    )
  )

  (:action load_ship
    :parameters (?p - package ?s - ship)
    :precondition (and
      (forall
        (?h - harbor) ; check that the package and the ship are in the same harbor
        (imply
          (at ?p ?h)
          (at ?s ?h)
        )
      )
      (not (in_vehicle ?p ?s)) ; check that the package is not already in the ship
    )
    :effect (and
      (in_vehicle ?p ?s) ; add the package to the ship
    )
  )

  (:action unload_vehicle
    :parameters (?p - package ?v - vehicle)
    :precondition (and
      (in_vehicle ?p ?v) ; check that the package is in the vehicle
    )
    :effect (and
      (not(in_vehicle ?p ?v)) ; remove the package from the vehicle
      (not(is_loaded_vehicle ?v)) ; set the vehicle as not loaded with a package

    )
  )
  (:action move_train
    :parameters (?t - train ?l1 - location ?l2 - location)
    :precondition (and
      (at ?t ?l1) ; check that the train is in the departure location
      ; (forall
      ;   (?w - wagon)
      ;   (imply
      ;     (connected_to_train ?w ?t)
      ;     (forall
      ;       (?p - package)
      ;       (imply
      ;         (in_vehicle ?p ?w)
      ;         (at ?p ?l1))
      ;     )
      ;   )
      ; )
    )
    :effect (and
      (not (at ?t ?l1)) ;  set that the train is not in the departing location
      (at ?t ?l2) ;  set that the train is in the destination location
      (forall
        (?w - wagon) ; set all wagons that are connected to the train to be in the destination location
        (when(connected_to_train ?w ?t)
          (forall ;  update the location of the packages carries in the wagons
            (?p - package)
            (when
              (in_vehicle ?p ?w)
              (at ?p ?l2)
              (not(at ?p ?l1))
            )
          )
          (not(at ?w ?l1))
          (at ?w ?l2)
        )
      )
    )
  )
  (:action move_ship
    :parameters (?s - ship ?h1 - harbor ?h2 - harbor)
    :precondition (and
      (at ?s ?h1) ; check that the ship is in the departure harbor
      (forall ; check that the 2 harbors are not in the same country
        (?c1 - city)
        (forall
          (?c2 - city)
          (imply
            (and
              (in_city ?h1 ?c1)
              (in_city ?h2 ?c2)
            )

            (forall
              (?cn1 - country)
              (forall
                (?cn2 - country)
                (imply
                  (and
                    (in_country ?c1 ?cn1)
                    (in_country ?c2 ?cn2)

                  )
                  (not(= ?cn1 ?cn2))
                )

              )
            )
          )

        )
      )
    )
    :effect (and
      (not (at ?s ?h1)) ; update the location of the ship
      (at ?s ?h2)
      (forall
        (?p - package) ; update the location of the packages carried by the ship
        (when
          (in_vehicle ?p ?s)
          (at ?p ?h2)
          (not(at ?p ?h1)))
      )
    )
  )

  (:action move_truck
    :parameters (?t - truck ?l1 - location ?l2 - location)
    :precondition (and
      (at ?t ?l1) ; check that the truck in the departing location
      (forall ; check that the departing and destination locations are in the same country
        (?c1 - city)
        (forall
          (?c2 - city)
          (imply
            (and
              (in_city ?l1 ?c1)
              (in_city ?l2 ?c2)
            )

            (forall
              (?cn1 - country)
              (forall
                (?cn2 - country)
                (imply
                  (and
                    (in_country ?c1 ?cn1)
                    (in_country ?c2 ?cn2)

                  )
                  (= ?cn1 ?cn2)
                )

              )
            )
          )

        )
      )
    )
    :effect (and
      (not (at ?t ?l1)) ; update the location of the truck
      (at ?t ?l2)
      (forall ; update the location of the packages
        (?p - package)
        (when
          (in_vehicle ?p ?t)
          (at ?p ?l2)
          (not (at ?p ?l1))
        )
      )
    )

  )

  (:action connect_wagon
    :parameters (?t - train_machine ?w - wagon)
    :precondition (and
      (forall ; check that the wagon and the train_machine are in the same location
        (?l - location)
        (imply
          (at ?t ?l)
          (at ?w ?l)
        )
      )
      (not (connected_to_train ?w ?t)) ; check that the wagon is not already connected to the train_machine

    )
    :effect (and
      (connected_to_train ?w ?t) ; connect the wagon to the train_machine
    )
  )

  (:action disconnect_wagon
    :parameters (?t - train_machine ?w - wagon)
    :precondition (and
      
      (connected_to_train ?w ?t) ; check that the wagon is connected to the train_machine

    )
    :effect (and
      (not(connected_to_train ?w ?t)) ; disconnect the wagon from the train_machine
    )
  )


)