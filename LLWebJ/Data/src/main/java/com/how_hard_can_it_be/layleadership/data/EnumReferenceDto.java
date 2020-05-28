package com.how_hard_can_it_be.layleadership.data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Objects;

@Entity( name = "EnumReference")
public class EnumReferenceDto
{
    @Id @Column( name = "EnumRefId")
    private long _id;

    @Column( name = "TableName")
    private String _tableName;

    @Column( name = "ColumnName")
    private String _columnName;

    @Column( name = "Value")
    private int _value;

    @Column( name = "Name")
    private String _name;

    @Column( name = "Description")
    private String _description;

    public EnumReferenceDto(){}

    public EnumReferenceDto(
            long anId,
            String aTableName,
            String aColumnName,
            int aValue,
            String aName,
            String aDescription)
    {
        _id          = anId;
        _tableName   = aTableName;
        _columnName  = aColumnName;
        _value       = aValue;
        _name        = aName;
        _description = aDescription;
    }

    public long getId()
    {
        return _id;
    }

    public String getTableName()
    {
        return _tableName;
    }

    public void setTableName(String aTableName)
    {
        _tableName = aTableName;
    }

    public String getColumnName()
    {
        return _columnName;
    }

    public void setColumnName(String aColumnName)
    {
        _columnName = aColumnName;
    }

    public int getValue()
    {
        return _value;
    }

    public void setValue(int aValue)
    {
        _value = aValue;
    }

    public String getName()
    {
        return _name;
    }

    public void setName(String aName)
    {
        _name = aName;
    }

    public String getDescription()
    {
        return _description;
    }

    public void setDescription(String aDescription)
    {
        _description = aDescription;
    }

    @Override
    public boolean equals(Object aO)
    {
        if (this == aO) return true;
        if (aO == null || getClass() != aO.getClass()) return false;
        EnumReferenceDto that = (EnumReferenceDto) aO;
        return _id == that._id &&
                _value == that._value &&
                _tableName.equals(that._tableName) &&
                _columnName.equals(that._columnName) &&
                _name.equals(that._name) &&
                Objects.equals(_description, that._description);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(_id, _tableName, _columnName, _value, _name, _description);
    }

    @Override
    public String toString()
    {
        return "EnumReferenceDto{" +
                "_id=" + _id +
                ", _tableName='" + _tableName + '\'' +
                ", _columnName='" + _columnName + '\'' +
                ", _value=" + _value +
                ", _name='" + _name + '\'' +
                ", _description='" + _description + '\'' +
                '}';
    }


}
