import * as dayjs from 'dayjs';

export interface IMessage {
  id?: number;
  value?: string | null;
  createdBy?: string | null;
  createdDate?: dayjs.Dayjs | null;
  lastModifiedBy?: string | null;
  lastModifiedDate?: dayjs.Dayjs | null;
  recordStatusId?: number;
}

export class Message implements IMessage {
  constructor(
    public id?: number,
    public value?: string | null,
    public createdBy?: string | null,
    public createdDate?: dayjs.Dayjs | null,
    public lastModifiedBy?: string | null,
    public lastModifiedDate?: dayjs.Dayjs | null,
    public recordStatusId?: number
  ) {}
}

export function getMessageIdentifier(message: IMessage): number | undefined {
  return message.id;
}
