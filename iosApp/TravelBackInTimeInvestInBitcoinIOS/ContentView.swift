//
//  ContentView.swift
//  TravelBackInTimeInvestInBitcoinIOS
//
//  Created by Vase4kin on 07.04.2021.
//

import SwiftUI

struct ContentView: View {
    let text: String
    let priceProviderName: String?
    let priceProviderURL: URL?
    
    var body: some View {
        VStack(spacing: 16) {
            Text(text)
            if let priceProviderName, let priceProviderURL {
                Link("Bitcoin price data by \(priceProviderName)", destination: priceProviderURL)
            }
        }
            .padding()
    }
}

struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        ContentView(
            text: "Test",
            priceProviderName: "Coin Metrics",
            priceProviderURL: URL(string: "https://coinmetrics.io/")
        )
    }
}
