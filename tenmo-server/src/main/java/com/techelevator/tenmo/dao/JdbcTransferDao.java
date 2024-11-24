package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcTransferDao implements TransferDao {
    private static final Logger log = LoggerFactory.getLogger(JdbcTransferDao.class);

    private final JdbcTemplate jdbcTemplate;

    public JdbcTransferDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Transfer> getTransfersByUserId(int userId) {
        // Step 1: Retrieve the accountId for the given userId
        String accountIdSql = "SELECT account_id FROM account WHERE user_id = ?";
        Integer accountId = jdbcTemplate.queryForObject(accountIdSql, Integer.class, userId);

        if (accountId == null) {
            log.warn("No account found for user ID {}", userId);
            return new ArrayList<>();  // Return an empty list if no account is found
        }

        // Step 2: Use the retrieved accountId to find the transfers
        List<Transfer> transfers = new ArrayList<>();
        String sql = "SELECT transfer_id, transfer_type_id, transfer_status_id, account_from, account_to, amount " +
                "FROM transfer WHERE account_from = ? OR account_to = ?";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, accountId, accountId);

        while (results.next()) {
            transfers.add(mapRowToTransfer(results));
        }

        log.info("Retrieved {} transfers for account ID {}", transfers.size(), accountId);  // Log the number of transfers found
        return transfers;
    }

    @Override
    public Transfer getTransferById(int transferId) {
        String sql = "SELECT transfer_id, transfer_type_id, transfer_status_id, account_from, account_to, amount " +
                "FROM transfer WHERE transfer_id = ?";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, transferId);
        if (results.next()) {
            return mapRowToTransfer(results);
        }
        return null; // or throw an exception if preferred
    }

    @Override
    public List<Transfer> findPendingRequestsByUserId(int userId) {
        // Step 1: Retrieve the accountId for the given userId
        String accountIdSql = "SELECT account_id FROM account WHERE user_id = ?";
        Integer accountId = jdbcTemplate.queryForObject(accountIdSql, Integer.class, userId);

        if (accountId == null) {
            log.warn("No account found for user ID {}", userId);
            return new ArrayList<>();  // Return an empty list if no account is found
        }

        // Step 2: Use the retrieved accountId to find the pending transfers
        List<Transfer> transfers = new ArrayList<>();
        String sql = "SELECT transfer_id, transfer_type_id, transfer_status_id, account_from, account_to, amount " +
                "FROM transfer WHERE account_to = ? AND transfer_status_id = 1";  // Assuming 1 is Pending
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, accountId);

        while (results.next()) {
            transfers.add(mapRowToTransfer(results));
        }

        log.info("Retrieved {} pending transfers for account ID {}", transfers.size(), accountId);  // Log the number of pending transfers found
        return transfers;
    }

    @Override
    public boolean createTransfer(Transfer transfer) {
        String sql = "INSERT INTO transfer (transfer_type_id, transfer_status_id, account_from, account_to, amount) " +
                "VALUES (?, ?, ?, ?, ?)";
        int rowsAffected = jdbcTemplate.update(sql,
                transfer.getTransferTypeId(),
                transfer.getTransferStatusId(),
                transfer.getAccountFromId(),
                transfer.getAccountToId(),
                transfer.getAmount());
        return rowsAffected > 0;
    }

    @Override
    public void updateTransferStatus(int transferId, int statusId) {
        String sql = "UPDATE transfer SET transfer_status_id = ? WHERE transfer_id = ?";
        jdbcTemplate.update(sql, statusId, transferId);
    }

    private Transfer mapRowToTransfer(SqlRowSet rowSet) {
        Transfer transfer = new Transfer();
        transfer.setTransferId(rowSet.getInt("transfer_id"));
        transfer.setTransferTypeId(rowSet.getInt("transfer_type_id"));
        transfer.setTransferStatusId(rowSet.getInt("transfer_status_id"));
        transfer.setAccountFrom(rowSet.getInt("account_from"));
        transfer.setAccountTo(rowSet.getInt("account_to"));
        transfer.setAmount(rowSet.getBigDecimal("amount"));
        return transfer;
    }
}
