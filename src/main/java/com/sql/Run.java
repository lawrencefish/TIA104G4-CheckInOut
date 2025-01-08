package com.sql;

public class Run {
    public static void main(String[] args) {
        RandomHotelPhotoUploader.run();
        HotelLicensePhotoWrite.run();
        RoomTypePhotoWrite.run();

        HotelFacilityUploader.run();
        RoomTypeFacilityUploader.run();

        RoomUploader.run();
        PriceUploader.run();

        System.out.println("All done");
    }
}
