package it.manueomm.facefile.converter;

import it.manueomm.facefile.bean.AlbumWrapper;
import it.manueomm.facefile.exceptions.ConvertException;

import java.io.File;

public interface IAlbumConverter {

   public File build(final AlbumWrapper album) throws ConvertException;

}
