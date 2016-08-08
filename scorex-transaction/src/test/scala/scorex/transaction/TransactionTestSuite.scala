package scorex.transaction

import org.scalatest.Suites
import scorex.transaction.state.database.blockchain.StoredBlockSeqSpecification

class TransactionTestSuite extends Suites(
  new TransactionSpecification,
  new StoredStateUnitTests,
  new RowSpecification,
  new GenesisTransactionSpecification,
  new UnconfirmedPoolSynchronizerSpecification,
  new StoredBlockSeqSpecification
)
