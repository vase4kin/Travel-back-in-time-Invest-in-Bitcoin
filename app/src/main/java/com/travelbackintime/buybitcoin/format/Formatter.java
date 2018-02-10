/*
 * Copyright 2018 Andrey Tolpeev
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.travelbackintime.buybitcoin.format;

import java.text.NumberFormat;
import java.util.Date;
import java.util.Locale;

public class Formatter {

    private final NumberFormat numberFormat;

    public Formatter(NumberFormat numberFormat) {
        this.numberFormat = numberFormat;
    }

    public String formatPrice(Double amount) {
        return numberFormat.format(amount);
    }

    public String formatPriceAsOnlyDigits(Double amount) {
        return String.format(Locale.US, "%,.2f", amount);
    }

    public String formatDate(Date date) {
        return String.format(Locale.US, "%1$tb %1$tY", date);
    }

    public String formatDateToShareText(Date date) {
        return String.format(Locale.US, "%1$tB %1$tY", date);
    }

    public String formatMonth(Date date) {
        return String.format(Locale.US, "%1$tb", date);
    }

    public String formatYear(Date date) {
        return String.format(Locale.US, "%1$tY", date);
    }
}
