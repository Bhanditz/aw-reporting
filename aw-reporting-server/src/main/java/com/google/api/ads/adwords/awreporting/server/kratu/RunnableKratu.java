// Copyright 2013 Google Inc. All Rights Reserved.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.api.ads.adwords.awreporting.server.kratu;

import com.google.api.ads.adwords.awreporting.server.entities.Account;
import com.google.api.ads.adwords.awreporting.server.entities.Kratu;
import com.google.api.ads.adwords.awreporting.server.util.StorageHelper;
import com.google.common.collect.Lists;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * KratuProcessor
 */
public class RunnableKratu implements Runnable {
  
  private CountDownLatch latch;
  
  Long topAccountId;
  Date dateStart;
  Date dateEnd;
  List<Account> accounts;

  StorageHelper storageHelper;

  public RunnableKratu(Long topAccountId, List<Account> accounts, StorageHelper storageHelper, Date dateStart, Date dateEnd) {
    
    super();
    this.topAccountId = topAccountId;
    this.dateStart = dateStart;
    this.dateEnd = dateEnd;
    this.storageHelper = storageHelper;
    this.accounts = accounts;
  }

  public void run() {
    try {     
      System.out.println("\n ** Generating Kratus (for: " + accounts.size() + ") **");
      long start = System.currentTimeMillis();

      // Make sure Index exists
      List<String> indexes = Lists.newArrayList();
      indexes.add(Kratu.EXTERNAL_CUSTOMER_ID);
      indexes.add(Kratu.DAY);
      storageHelper.getEntityPersister().createIndex(Kratu.class, indexes);

      // Get all the (not-MCC) Accounts under TopAccount
      int i = 0;
      for (Account account : accounts) {
        if (account != null && !account.getCanManageClients()) {
          System.out.println();
          System.out.print(i++);

          Calendar dayToProcess = Calendar.getInstance();
          dayToProcess.setTime(dateStart);
          dayToProcess.set(Calendar.HOUR_OF_DAY, 0);
          dayToProcess.set(Calendar.MINUTE, 0);
          dayToProcess.set(Calendar.SECOND, 0);
          dayToProcess.add(Calendar.HOUR, 12);

          Calendar lastDay = Calendar.getInstance();
          lastDay.setTime(dateEnd);
          lastDay.set(Calendar.HOUR_OF_DAY, 23);
          lastDay.set(Calendar.MINUTE, 59);
          lastDay.set(Calendar.SECOND, 59);
          lastDay.add(Calendar.HOUR, 12);

          while(dayToProcess.compareTo(lastDay) <= 0) {

            Kratu kratu = KratuCompute.createDailyKratuFromDB(
                storageHelper, topAccountId, account, dayToProcess.getTime());
            if (kratu != null){
              storageHelper.saveKratu(kratu);
            }

            // Process next day
            dayToProcess.add(Calendar.DATE, 1);
            System.out.print(".");
          }
        }
      }

      System.out.println("\n*** Finished generating Kratus in "
          + ((System.currentTimeMillis() - start) / 1000) + " seconds ***");
      System.out.println();

    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      if (this.latch != null) {
        this.latch.countDown();
      }
    }
  }

  /**
   * @param latch the latch to set
   */
  public void setLatch(CountDownLatch latch) {
    this.latch = latch;
  }
}