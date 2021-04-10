//
//  TravelBackInTimeInvestInBitcoinIOSApp.swift
//  TravelBackInTimeInvestInBitcoinIOS
//
//  Created by Vase4kin on 07.04.2021.
//

import SwiftUI
import Firebase
import timetravelmachine

@main
struct TravelBackInTimeInvestInBitcoinIOSApp: App {
    
    @State var placeHolder = ContentView(text: "Loading")
    
    var body: some Scene {
        WindowGroup {
            placeHolder.onAppear(perform: {
                getData { (data) in
                    self.placeHolder = ContentView(text: data)
                }
            })
        }
    }
    
    func getData(_ completion: @escaping ((String) -> Void)) {
        FirebaseApp.configure()
        let eventWithAbsentPrice = TimeTravelMachineEvent.RealWorldEvent.init(title: "Oops", description: "No price available!", isDonate: false)
        let timeTravelMachine = TimeTravelMachineFactory.init().create(eventWithAbsentPrice: eventWithAbsentPrice)
        timeTravelMachine.travelInTime(
            time: TimeTravelConstraints.init().maxDateTimeInMillis,
            investedMoney: 99.9
        ) {
            (event: TimeTravelMachineEvent?, error: Error?) in
            completion(event?.description ?? "Error")
        }
    }
}
