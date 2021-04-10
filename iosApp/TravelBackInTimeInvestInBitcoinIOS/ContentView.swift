//
//  ContentView.swift
//  TravelBackInTimeInvestInBitcoinIOS
//
//  Created by Vase4kin on 07.04.2021.
//

import SwiftUI

struct ContentView: View {
    
    var text: String
    
    var body: some View {
        Text(text)
            .padding()
    }
}

struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        ContentView(text: "Test")
    }
}
