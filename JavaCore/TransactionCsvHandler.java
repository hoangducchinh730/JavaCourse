package JavaCore;

import java.time.LocalDate;

public class TransactionCsvHandler extends AbstractCsvHandler<Transaction>
{
    public Transaction parseLine(String line)
    {
        String[] part = line.split(",");

        if(part.length != 7)
            return null;

        String id = part[0];
        String vehicleId = part[1];
        String customerName = part[2];

        LocalDate startDate = LocalDate.parse(part[3]);

        LocalDate returnDate = null;
        if(!part[4].equals("null") && !part[4].isEmpty())
            returnDate = LocalDate.parse(part[4]);

        double totalPrice = Double.parseDouble(part[5]);
        boolean isReturned = Boolean.parseBoolean(part[6]);

        return new Transaction(id, vehicleId, customerName, startDate, returnDate, totalPrice, isReturned);
    }

    public String toCsv(Transaction item)
    {
        return String.join(",",
                item.getId(),
                item.getVehicleId(),
                item.getCustomerName(),
                item.getStartDate().toString(),
                item.getReturnDate() == null ? "null" : item.getReturnDate().toString(),
                String.valueOf(item.getTotalPrice()), // Ép double sang String
                String.valueOf(item.isReturned()) // Ép boolean sang String
        );
    }
}
