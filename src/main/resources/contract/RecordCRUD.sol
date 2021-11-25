pragma solidity>=0.4.24 <0.6.11;
pragma experimental ABIEncoderV2;

import "./Table.sol";


contract RecordCRUD {
    
    event CreateResult(int256 count);
    event InsertResult(int256 count);
    event UpdateResult(int256 count);
    event RemoveResult(int256 count);

    TableFactory tableFactory;
    string constant TABLE_name = "records";
    constructor() public {
        tableFactory = TableFactory(0x1001); //The fixed address is 0x1001 for TableFactory
        // the parameters of createTable are tableuid,keyField,"vlaueFiled1,vlaueFiled2,vlaueFiled3,..."
        tableFactory.createTable(TABLE_name, "main_tx_hash", "uid,locate");
    }

    //select records
    function select(string memory main_tx_hash)
    public
    view
    returns (string[] memory, string[] memory, string[] memory)
    {
        Table table = tableFactory.openTable(TABLE_name);

        Condition condition = table.newCondition();

        Entries entries = table.select(main_tx_hash, condition);
        string[] memory records_bytes_list = new string[](uint256(entries.size()));
        string[] memory uid_list = new string[](uint256(entries.size()));
        string[] memory locate_list = new string[](uint256(entries.size()));

        for (int256 i = 0; i < entries.size(); ++i) {
            Entry entry = entries.get(i);
            records_bytes_list[uint256(i)] = entry.getString("main_tx_hash");
            uid_list[uint256(i)] = entry.getString("uid");
            locate_list[uint256(i)] = entry.getString("locate");
        }

        return (records_bytes_list, uid_list, locate_list);
    }
    
     //insert records
    function insert(string memory main_tx_hash, string memory uid, string memory locate)
    public
    returns (int256)
    {
        Table table = tableFactory.openTable(TABLE_name);

        Entry entry = table.newEntry();
        entry.set("main_tx_hash", main_tx_hash);
        entry.set("uid", uid);
        entry.set("locate",locate);

        int256 count = table.insert(main_tx_hash, entry);
        emit InsertResult(count);

        return count;
    }
    //update records
    function update(string memory main_tx_hash, string memory uid, string memory locate)
    public
    returns (int256)
    {
        Table table = tableFactory.openTable(TABLE_name);

        Entry entry = table.newEntry();
        entry.set("uid", uid);
        entry.set("locate", locate);

        Condition condition = table.newCondition();
        condition.EQ("main_tx_hash", main_tx_hash);

        int256 count = table.update(main_tx_hash, entry, condition);
        emit UpdateResult(count);

        return count;
    }
    //remove records
    function remove(string memory main_tx_hash) public returns (int256) {
        Table table = tableFactory.openTable(TABLE_name);

        Condition condition = table.newCondition();
        condition.EQ("main_tx_hash", main_tx_hash);

        int256 count = table.remove(main_tx_hash, condition);
        emit RemoveResult(count);

        return count;
    }
    
   
}