//
//  TravelBackInTimeInvestInBitcoinIOSApp.swift
//  TravelBackInTimeInvestInBitcoinIOS
//
//  Created by Vase4kin on 07.04.2021.
//

import SwiftUI
import timetravelmachine

@main
struct TravelBackInTimeInvestInBitcoinIOSApp: App {
    @State private var result = ResultContent.loading
    
    var body: some Scene {
        WindowGroup {
            ContentView(
                text: result.text,
                priceProviderName: result.priceProviderName,
                priceProviderURL: result.priceProviderURL
            )
            .onAppear {
                getData { content in
                    DispatchQueue.main.async {
                        result = content
                    }
                }
            }
        }
    }

    private func getData(_ completion: @escaping (ResultContent) -> Void) {
        let timeTravelMachine = TimeTravelMachineFactory().create()
        timeTravelMachine.travelInTime(
            time: TimeTravelConstraints().maxDateTimeInMillis,
            investedMoney: 99.9
        ) { event, _ in
            guard let event = event as? TimeTravelMachineEvent.TimeTravelEvent else {
                completion(.error)
                return
            }
            completion(
                ResultContent(
                    text: event.description(),
                    priceProviderName: event.priceProvider.displayName,
                    priceProviderURL: URL(string: event.priceProvider.websiteUrl)
                )
            )
        }
    }
}

private struct ResultContent {
    let text: String
    let priceProviderName: String?
    let priceProviderURL: URL?

    static let loading = ResultContent(text: "Loading", priceProviderName: nil, priceProviderURL: nil)
    static let error = ResultContent(text: "Error", priceProviderName: nil, priceProviderURL: nil)
}
