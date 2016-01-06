package de.unistuttgart.ims.fictionnet.databaseaccess;

import java.security.InvalidParameterException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.dao.Dao.CreateOrUpdateStatus;
import com.j256.ormlite.support.ConnectionSource;

import de.unistuttgart.ims.fictionnet.datastructure.LayerContainer;
import de.unistuttgart.ims.fictionnet.datastructure.Text;
import de.unistuttgart.ims.fictionnet.datastructure.layers.ActLayer;
import de.unistuttgart.ims.fictionnet.datastructure.layers.EventLayer;
import de.unistuttgart.ims.fictionnet.datastructure.layers.InteractionLayer;
import de.unistuttgart.ims.fictionnet.datastructure.layers.Layer;
import de.unistuttgart.ims.fictionnet.datastructure.layers.TalksAboutLayer;
import de.unistuttgart.ims.fictionnet.datastructure.layers.PresenceLayer;
import de.unistuttgart.ims.fictionnet.datastructure.layers.SceneLayer;
import de.unistuttgart.ims.fictionnet.datastructure.layers.SentenceLayer;
import de.unistuttgart.ims.fictionnet.datastructure.layers.SpeakerLayer;
import de.unistuttgart.ims.fictionnet.datastructure.layers.TokenLayer;
import de.unistuttgart.ims.fictionnet.datastructure.layers.TypeOfTextLayer;

/**
 * @author Lukas Rieger, Domas Mikalkinas
 * @version 27-10-2015
 * 
 *          This class represents the table that stores the layerContainers. To
 *          make it easier to use the content of the containers is automatically
 *          saved/loaded in extra tables for each kind of layer.
 *
 */
public class DataTableLayercontainer extends AbstractDataTable<LayerContainer, Integer> {
	private static final String INCONSISTENCY_EXCEPTION_MESSAGE = "Inconsistency in mentionlayer table";
	private transient Dao<TypeOfTextLayer, Integer> dbTypeOfTextLayerDao;
	private transient Dao<TokenLayer, Integer> dbTokenLayerDao;
	private transient Dao<SentenceLayer, Integer> dbSentenceLayerDao;
	private transient Dao<PresenceLayer, Integer> dbPresenceLayerDao;
	private transient Dao<TalksAboutLayer, Integer> dbTalksAboutLayerDao;
	private transient Dao<InteractionLayer, Integer> dbInteractionLayerDao;
	private transient Dao<EventLayer, Integer> dbEventLayerDao;
	private transient Dao<ActLayer, Integer> dbActLayerDao;
	private transient Dao<SceneLayer, Integer> dbSceneLayerDao;
	private transient Dao<SpeakerLayer, Integer> dbSpeakerLayerDao;

	/**
	 * public constructor for the concrete datatables
	 */
	public DataTableLayercontainer() {
		super(LayerContainer.class);
	}

	/**
	 * Sets the connection source for this table and all subtables
	 * 
	 * @throws SQLException
	 */
	@Override
	public void setConnectionSource(final ConnectionSource connectionSource) throws SQLException {
		this.connectionSource = connectionSource;
		this.dao = DaoManager.createDao(connectionSource, type);
		this.deleteBuilder = dao.deleteBuilder();
		this.queryBuilder = dao.queryBuilder();

		dbTypeOfTextLayerDao = DaoManager.createDao(connectionSource, TypeOfTextLayer.class);
		dbTokenLayerDao = DaoManager.createDao(connectionSource, TokenLayer.class);
		dbSentenceLayerDao = DaoManager.createDao(connectionSource, SentenceLayer.class);
		dbPresenceLayerDao = DaoManager.createDao(connectionSource, PresenceLayer.class);
		dbTalksAboutLayerDao = DaoManager.createDao(connectionSource, TalksAboutLayer.class);
		dbInteractionLayerDao = DaoManager.createDao(connectionSource, InteractionLayer.class);
		dbEventLayerDao = DaoManager.createDao(connectionSource, EventLayer.class);
		dbActLayerDao = DaoManager.createDao(connectionSource, ActLayer.class);
		dbSceneLayerDao = DaoManager.createDao(connectionSource, SceneLayer.class);
		dbSpeakerLayerDao = DaoManager.createDao(connectionSource, SpeakerLayer.class);

	}

	/**
	 * Creates or updates the given layercontainer in the database. This
	 * includes all the layers contained in the container.
	 * 
	 * @throws SQLException
	 */
	public boolean createOrUpdate(final LayerContainer data, Text text) throws SQLException {
		boolean result = false;
		if (dao != null) {
			final ArrayList<Layer> layers = data.getLayers();
			CreateOrUpdateStatus status;
			for (final Layer layer : layers) {

				try {
					Logger.getLogger(getClass().getName()).log(Level.INFO, "Number of layers: " + layers.size());
					Logger.getLogger(getClass().getName()).log(Level.INFO, "Save " + layer.getClass().getName());
					saveLayer(layer, text, false);
				} catch (SQLException e) {
					Logger.getLogger(getClass().getName()).log(Level.SEVERE, "SQL Error on layer update.", e);
				}
			}
			status = dao.createOrUpdate(data);
			result = status.getNumLinesChanged() == 1;
		}
		return result;
	}

	/**
	 * Saves the layercontainer with its layers that belongs to the text.
	 * 
	 * @param data
	 * @param text
	 * @return
	 * @throws SQLException
	 */
	public boolean create(final LayerContainer data, Text text) throws SQLException {
		boolean result = false;
		if (dao != null) {
			int status = dao.create(data);
			final ArrayList<Layer> layers = data.getLayers();
			for (final Layer layer : layers) {
				layer.setContainerId(data.getId());
				try {
					Logger.getLogger(getClass().getName()).log(Level.INFO, "Number of layers: " + layers.size());
					Logger.getLogger(getClass().getName()).log(Level.INFO, "Save " + layer.getClass().getName());
					saveLayer(layer, text, true);
				} catch (SQLException e) {
					Logger.getLogger(getClass().getName()).log(Level.SEVERE, "SQL Error on layer update.", e);
				}
			}
			result = status == 1;
		}
		return result;
	}

	/**
	 * Deletes the given layer container from the table and all the contained
	 * layers from their tables.
	 * 
	 * @throws SQLException
	 */
	@Override
	public boolean delete(final LayerContainer data) throws SQLException {
		boolean result = false;
		if (dao != null && data != null) {
			for (final Layer layer : data.getLayers()) {
				deleteLayer(layer);
			}
			final int deletedRowsCount = dao.delete(data);
			result = deletedRowsCount == 1;
		}
		return result;
	}

	/**
	 * Loads the layers and their content of this text.
	 * 
	 * @param text
	 * @return
	 * @throws SQLException
	 * @throws Exception
	 */
	public Text loadLayerContainer(final Text text) throws SQLException {
		final String columnName = DBTableColumns.TEXTID.toString();
		final int textId = text.getId();
		final LayerContainer layerContainer = selectFromThisAllWhere().eq(columnName, textId).queryForFirst();
		if (layerContainer == null) {
			Logger.getLogger(getClass().getName()).log(Level.WARNING, "Could not load layers.");
		} else {
			setTokenLayer(layerContainer);
			setSentenceLayer(layerContainer);
			setPresenceLayer(layerContainer);
			setTalksAboutLayer(layerContainer);
			setInteractionLayer(layerContainer);
			setEventLayer(layerContainer);
			setActLayer(layerContainer);
			setTypeOfTextLayer(layerContainer);
			setSceneLayer(layerContainer);
			setSpeakerLayer(layerContainer);
			text.setLayerContainer(layerContainer);
		}
		return text;
	}

	/**
	 * Loads the content of the given layer from the database.
	 * 
	 * @param layerContainer
	 * @throws Exception
	 */
	private void setActLayer(final LayerContainer layerContainer) throws SQLException {
		final String columnNameContId = DBTableColumns.CONTAINERID.toString();
		final ArrayList<ActLayer> actLayer = (ArrayList<ActLayer>) dbActLayerDao.queryForEq(columnNameContId,
				layerContainer.getId());
		if (actLayer == null) {
			return;
		}
		if (actLayer.size() > 1) {
			throw new SQLException(INCONSISTENCY_EXCEPTION_MESSAGE);

		}
		if (actLayer.size() == 1) {
			layerContainer.setLayer(actLayer.get(0));

		}

	}

	/**
	 * Loads the content of the given layer from the database.
	 * 
	 * @param layerContainer
	 * @throws Exception
	 */
	private void setEventLayer(final LayerContainer layerContainer) throws SQLException {
		final String columnNameContId = DBTableColumns.CONTAINERID.toString();
		final ArrayList<EventLayer> eventLayer = (ArrayList<EventLayer>) dbEventLayerDao.queryForEq(columnNameContId,
				layerContainer.getId());
		if (eventLayer == null) {
			return;
		}
		if (eventLayer.size() > 1) {
			throw new SQLException(INCONSISTENCY_EXCEPTION_MESSAGE);
		}
		if (eventLayer.size() == 1) {
			layerContainer.setLayer(eventLayer.get(0));

		}
	}

	/**
	 * Loads the content of the given layer from the database.
	 * 
	 * @param layerContainer
	 * @throws Exception
	 */
	private void setInteractionLayer(final LayerContainer layerContainer) throws SQLException {
		final String columnNameContId = DBTableColumns.CONTAINERID.toString();
		final ArrayList<InteractionLayer> interactionLayer = (ArrayList<InteractionLayer>) dbInteractionLayerDao
				.queryForEq(columnNameContId, layerContainer.getId());
		if (interactionLayer == null) {
			return;
		}
		if (interactionLayer.size() > 1) {
			throw new SQLException(INCONSISTENCY_EXCEPTION_MESSAGE);
		}
		if (interactionLayer.size() == 1) {

			layerContainer.setLayer(interactionLayer.get(0));

		}
	}

	/**
	 * Loads the content of the given layer from the database.
	 * 
	 * @param layerContainer
	 * @throws Exception
	 */
	private void setTokenLayer(final LayerContainer layerContainer) throws SQLException {

		final String columnNameContId = DBTableColumns.CONTAINERID.toString();
		final ArrayList<TokenLayer> tokenLayer = (ArrayList<TokenLayer>) dbTokenLayerDao.queryForEq(columnNameContId,
				layerContainer.getId());
		if (tokenLayer == null) {
			return;
		}
		if (tokenLayer.size() > 1) {
			throw new SQLException("Inconsistency in tokenlayer table");
		}
		if (tokenLayer.size() == 1) {
			layerContainer.setLayer(tokenLayer.get(0));

		}
	}

	/**
	 * Loads the content of the given layer from the database.
	 * 
	 * @param layerContainer
	 * @throws Exception
	 */
	private void setSentenceLayer(final LayerContainer layerContainer) throws SQLException {

		final String columnNameContId = DBTableColumns.CONTAINERID.toString();
		final ArrayList<SentenceLayer> sentenceLayer = (ArrayList<SentenceLayer>) dbSentenceLayerDao
				.queryForEq(columnNameContId, layerContainer.getId());
		if (sentenceLayer == null) {
			return;
		}
		if (sentenceLayer.size() > 1) {
			throw new SQLException("Inconsistency in sentencelayer table");
		}
		if (sentenceLayer.size() == 1) {
			layerContainer.setLayer(sentenceLayer.get(0));

		}
	}

	/**
	 * Loads the content of the given layer from the database.
	 * 
	 * @param layerContainer
	 * @throws Exception
	 */
	private void setPresenceLayer(final LayerContainer layerContainer) throws SQLException {
		final String columnNameContId = DBTableColumns.CONTAINERID.toString();
		final ArrayList<PresenceLayer> presenceLayer = (ArrayList<PresenceLayer>) dbPresenceLayerDao
				.queryForEq(columnNameContId, layerContainer.getId());
		if (presenceLayer == null) {
			return;
		}
		if (presenceLayer.size() > 1) {
			throw new SQLException("Inconsistency in presencelayer table");
		}
		if (presenceLayer.size() == 1) {
			layerContainer.setLayer(presenceLayer.get(0));

		}
	}

	/**
	 * Loads the content of the given layer from the database.
	 * 
	 * @param layerContainer
	 * @throws SQLException
	 */
	private void setTalksAboutLayer(final LayerContainer layerContainer) throws SQLException {
		final String columnNameContId = DBTableColumns.CONTAINERID.toString();
		final ArrayList<TalksAboutLayer> talksAboutLayer = (ArrayList<TalksAboutLayer>) dbTalksAboutLayerDao
				.queryForEq(columnNameContId, layerContainer.getId());
		if (talksAboutLayer == null) {
			return;
		}
		if (talksAboutLayer.size() > 1) {
			throw new SQLException(INCONSISTENCY_EXCEPTION_MESSAGE);
		}
		if (talksAboutLayer.size() == 1) {
			layerContainer.setLayer(talksAboutLayer.get(0));

		}
	}

	/**
	 * Loads the content of the given layer from the database.
	 * 
	 * @param layerContainer
	 * @throws SQLException
	 */
	private void setSceneLayer(final LayerContainer layerContainer) throws SQLException {
		final String columnNameContId = DBTableColumns.CONTAINERID.toString();
		final ArrayList<SceneLayer> sceneLayer = (ArrayList<SceneLayer>) dbSceneLayerDao.queryForEq(columnNameContId,
				layerContainer.getId());
		if (sceneLayer == null) {
			return;
		}
		if (sceneLayer.size() > 1) {
			throw new SQLException(INCONSISTENCY_EXCEPTION_MESSAGE);
		}
		if (sceneLayer.size() == 1) {
			layerContainer.setLayer(sceneLayer.get(0));

		}
	}

	/**
	 * Loads the content of the given layer from the database.
	 * 
	 * @param layerContainer
	 * @throws SQLException
	 */
	private void setTypeOfTextLayer(final LayerContainer layerContainer) throws SQLException {
		final String columnNameContId = DBTableColumns.CONTAINERID.toString();
		final ArrayList<TypeOfTextLayer> typeOfTextLayer = (ArrayList<TypeOfTextLayer>) dbTypeOfTextLayerDao
				.queryForEq(columnNameContId, layerContainer.getId());
		if (typeOfTextLayer == null) {
			return;
		}
		if (typeOfTextLayer.size() > 1) {
			throw new SQLException(INCONSISTENCY_EXCEPTION_MESSAGE);
		}
		if (typeOfTextLayer.size() == 1) {
			layerContainer.setLayer(typeOfTextLayer.get(0));

		}
	}

	/**
	 * Loads the content of the given layer from the database.
	 * 
	 * @param layerContainer
	 * @throws SQLException
	 */
	private void setSpeakerLayer(final LayerContainer layerContainer) throws SQLException {
		final String columnNameContId = DBTableColumns.CONTAINERID.toString();
		final ArrayList<SpeakerLayer> speakerLayer = (ArrayList<SpeakerLayer>) dbSpeakerLayerDao
				.queryForEq(columnNameContId, layerContainer.getId());
		if (speakerLayer == null) {
			return;
		}
		if (speakerLayer.size() > 1) {
			throw new SQLException(INCONSISTENCY_EXCEPTION_MESSAGE);
		}
		if (speakerLayer.size() == 1) {
			layerContainer.setLayer(speakerLayer.get(0));

		}
	}

	private void saveActLayer(final ActLayer actLayer, final Text text, final boolean create) throws SQLException {
		if (create) {
			dbActLayerDao.create(actLayer);
		} else {
			dbActLayerDao.createOrUpdate(actLayer);
		}

		DBAccessManager.getTheInstance().increaseProgress(text);

	}

	private void saveTokenLayer(final TokenLayer tokenLayer, final Text text, final boolean create)
			throws SQLException {
		if (create) {
			dbTokenLayerDao.create(tokenLayer);
		} else {
			dbTokenLayerDao.createOrUpdate(tokenLayer);
		}

		DBAccessManager.getTheInstance().increaseProgress(text);

	}

	private void saveSentenceLayer(final SentenceLayer sentenceLayer, final Text text, final boolean create)
			throws SQLException {
		if (create) {
			dbSentenceLayerDao.create(sentenceLayer);
		} else {
			dbSentenceLayerDao.createOrUpdate(sentenceLayer);
		}

		DBAccessManager.getTheInstance().increaseProgress(text);

	}

	private void saveTypeOfTextLayer(final TypeOfTextLayer typeOfTextLayer, final Text text, final boolean create)
			throws SQLException {
		if (create) {
			dbTypeOfTextLayerDao.create(typeOfTextLayer);
		} else {
			dbTypeOfTextLayerDao.createOrUpdate(typeOfTextLayer);
		}

		DBAccessManager.getTheInstance().increaseProgress(text);

	}

	private void savePresenceLayer(final PresenceLayer presenceLayer, final Text text, final boolean create)
			throws SQLException {
		if (create) {
			dbPresenceLayerDao.create(presenceLayer);
		} else {
			dbPresenceLayerDao.createOrUpdate(presenceLayer);
		}

		DBAccessManager.getTheInstance().increaseProgress(text);

	}

	private void saveInteractionLayer(final InteractionLayer interactionLayer, final Text text, final boolean create)
			throws SQLException {
		if (create) {
			dbInteractionLayerDao.create(interactionLayer);
		} else {
			dbInteractionLayerDao.createOrUpdate(interactionLayer);
		}
		DBAccessManager.getTheInstance().increaseProgress(text);
	}

	private void saveEventLayer(final EventLayer eventLayer, final Text text, final boolean create)
			throws SQLException {
		if (create) {
			dbEventLayerDao.create(eventLayer);
		} else {
			dbEventLayerDao.createOrUpdate(eventLayer);
		}

		DBAccessManager.getTheInstance().increaseProgress(text);

	}

	private void saveSceneLayer(final SceneLayer sceneLayer, final Text text, final boolean create)
			throws SQLException {
		if (create) {
			dbSceneLayerDao.create(sceneLayer);
		} else {
			dbSceneLayerDao.createOrUpdate(sceneLayer);
		}

		DBAccessManager.getTheInstance().increaseProgress(text);

	}

	private void saveTalksAboutLayer(final TalksAboutLayer talksAboutLayer, final Text text, final boolean create)
			throws SQLException {
		if (create) {
			dbTalksAboutLayerDao.create(talksAboutLayer);
		} else {
			dbTalksAboutLayerDao.createOrUpdate(talksAboutLayer);
		}

		DBAccessManager.getTheInstance().increaseProgress(text);

	}

	private void saveSpeakerLayer(final SpeakerLayer speakerLayer, final Text text, final boolean create)
			throws SQLException {
		if (create) {
			dbSpeakerLayerDao.create(speakerLayer);
		} else {
			dbSpeakerLayerDao.createOrUpdate(speakerLayer);
		}

		DBAccessManager.getTheInstance().increaseProgress(text);

	}

	/**
	 * Saves the given layer to the database and returns the id that the
	 * database gave to the layer.
	 * 
	 * @param layer
	 * @return layer id in db
	 * @throws SQLException
	 */
	private int saveLayer(final Layer layer, final Text text, final boolean create) throws SQLException {
		if (layer != null) {
			if (layer instanceof ActLayer) {
				final ActLayer actLayer = (ActLayer) layer;
				saveActLayer(actLayer, text, create);
			} else if (layer instanceof TokenLayer) {
				final TokenLayer tokenLayer = (TokenLayer) layer;
				saveTokenLayer(tokenLayer, text, create);
			} else if (layer instanceof SentenceLayer) {
				final SentenceLayer sentenceLayer = (SentenceLayer) layer;
				saveSentenceLayer(sentenceLayer, text, create);
			} else if (layer instanceof TypeOfTextLayer) {
				final TypeOfTextLayer typeOfTextLayer = (TypeOfTextLayer) layer;
				saveTypeOfTextLayer(typeOfTextLayer, text, create);
			} else if (layer instanceof PresenceLayer) {
				final PresenceLayer presenceLayer = (PresenceLayer) layer;
				savePresenceLayer(presenceLayer, text, create);
			} else if (layer instanceof InteractionLayer) {
				final InteractionLayer interactionLayer = (InteractionLayer) layer;
				saveInteractionLayer(interactionLayer, text, create);
			} else if (layer instanceof EventLayer) {
				final EventLayer eventLayer = (EventLayer) layer;
				saveEventLayer(eventLayer, text, create);
			} else if (layer instanceof SceneLayer) {
				final SceneLayer sceneLayer = (SceneLayer) layer;
				saveSceneLayer(sceneLayer, text, create);
			} else if (layer instanceof SpeakerLayer) {
				final SpeakerLayer speakerLayer = (SpeakerLayer) layer;
				saveSpeakerLayer(speakerLayer, text, create);
			} else {
				final TalksAboutLayer talksAboutLayer = (TalksAboutLayer) layer;
				saveTalksAboutLayer(talksAboutLayer, text, create);
			}
			return layer.getLayerId();
		}
		throw new InvalidParameterException();
	}

	/**
	 * Deletes the given layer from the database
	 * 
	 * @param layer
	 * @return layer id in db
	 * @throws SQLException
	 */
	boolean deleteLayer(final Layer layer) throws SQLException {
		int changedRowsCount = 0;
		if (layer != null) {
			if (layer instanceof ActLayer) {
				ActLayer actLayer = (ActLayer) layer;

				changedRowsCount = dbActLayerDao.delete(actLayer);

			} else if (layer instanceof TokenLayer) {
				TokenLayer tokenLayer = (TokenLayer) layer;

				changedRowsCount = dbTokenLayerDao.delete(tokenLayer);

			} else if (layer instanceof SentenceLayer) {
				SentenceLayer sentenceLayer = (SentenceLayer) layer;

				changedRowsCount = dbSentenceLayerDao.delete(sentenceLayer);

			} else if (layer instanceof TypeOfTextLayer) {
				TypeOfTextLayer typeOfTextLayer = (TypeOfTextLayer) layer;

				changedRowsCount = dbTypeOfTextLayerDao.delete(typeOfTextLayer);

			} else if (layer instanceof PresenceLayer) {
				PresenceLayer presenceLayer = (PresenceLayer) layer;

				changedRowsCount = dbPresenceLayerDao.delete(presenceLayer);

			} else if (layer instanceof InteractionLayer) {

				InteractionLayer interactionLayer = (InteractionLayer) layer;
				changedRowsCount = dbInteractionLayerDao.delete(interactionLayer);

			} else if (layer instanceof EventLayer) {
				EventLayer eventLayer = (EventLayer) layer;

				changedRowsCount = dbEventLayerDao.delete(eventLayer);

			} else if (layer instanceof TalksAboutLayer) {
				TalksAboutLayer talksAboutLayer = (TalksAboutLayer) layer;

				changedRowsCount = dbTalksAboutLayerDao.delete(talksAboutLayer);

			} else if (layer instanceof SceneLayer) {
				SceneLayer sceneLayer = (SceneLayer) layer;

				changedRowsCount = dbSceneLayerDao.delete(sceneLayer);

			} else if (layer instanceof TypeOfTextLayer) {
				TypeOfTextLayer typeOfTextLayer = (TypeOfTextLayer) layer;

				changedRowsCount = dbTypeOfTextLayerDao.delete(typeOfTextLayer);

			} else if (layer instanceof SpeakerLayer) {
				SpeakerLayer speakerLayer = (SpeakerLayer) layer;

				changedRowsCount = dbSpeakerLayerDao.delete(speakerLayer);

			} else {
				throw new InvalidParameterException();
			}
		}
		return changedRowsCount == 1;
	}

	/**
	 * Deletes the layerContainer in the text. It is deleted with all its
	 * database entries.
	 * 
	 * @param text
	 * @throws SQLException
	 */
	public void deleteLayerContainer(final Text text) throws SQLException {
		final LayerContainer layerContainer = text.getLayerContainer();
		dbTokenLayerDao.delete(layerContainer.getTokenLayer());
		dbTypeOfTextLayerDao.delete(layerContainer.getTypeOfTextLayer());
		dbSentenceLayerDao.delete(layerContainer.getSentenceLayer());
		dbPresenceLayerDao.delete(layerContainer.getPresenceLayer());
		dbTalksAboutLayerDao.delete(layerContainer.getTalksAboutLayer());
		dbInteractionLayerDao.delete(layerContainer.getInteractionLayer());
		dbEventLayerDao.delete(layerContainer.getEventLayer());
		dbActLayerDao.delete(layerContainer.getActLayer());
		dbSceneLayerDao.delete(layerContainer.getSceneLayer());
		dbTypeOfTextLayerDao.delete(layerContainer.getTypeOfTextLayer());
		dbSpeakerLayerDao.delete(layerContainer.getSpeakerLayer());

		dao.delete(layerContainer);
		text.setLayerContainer(null);
	}
}
